package grainalcohol.oap.power;

import grainalcohol.oap.mixin.MobEntityMixin;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

import java.util.function.Predicate;

/**
 * 类型ID: oap:modify_mob_behavior<br>
 * <br>
 * 修改怪物对能力持有者的行为<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li><b>friendly</b>: 变为友好状态，不会攻击能力持有者</li>
 *     <li><b>passive</b>: 变为被动状态，不会主动攻击能力持有者</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>entity_condition</b> ({@code EntityCondition}, 可选): 通过匹配的实体才会被应用修改</li>
 *   <li><b>behavior</b> ({@code String}, 必选): 修改结果，接受“friendly”或“passive”</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * // 使所有一般敌对生物友好化
 * {
 *   "type": "oap:modify_mob_behavior",
 *   "behavior": "FRIENDLY"
 * }
 * }</pre>
 *
 * @see MobEntityMixin 实际触发逻辑的Mixin类
 */
public class ModifyMobBehaviorPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("behavior", SerializableDataTypes.STRING);

    private final Predicate<Entity> entity_condition;
    private final EntityBehavior behavior;

    public EntityBehavior getBehavior() {
        return this.behavior;
    }

    public ModifyMobBehaviorPower(PowerType<?> type, LivingEntity entity, Predicate<Entity> entityCondition, String behavior) {
        super(type, entity);
        entity_condition = entityCondition;
        this.behavior = EntityBehavior.valueOf(behavior.toUpperCase());
    }

    public boolean shouldApply(MobEntity mob) {
        return entity_condition == null || entity_condition.test(mob);
    }

    public enum EntityBehavior {
        FRIENDLY,
        PASSIVE
    }
}
