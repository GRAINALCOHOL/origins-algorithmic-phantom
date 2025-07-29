package grainalcohol.oap.power;

import grainalcohol.oap.mixin.LivingEntityMixin;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 类型ID: oap:action_on_death<br>
 * <br>
 * 实体死亡时的操作<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>damage_condition字段中amount类型检查将始终收到0</li>
 *     <li>如果存在默认条件字段则需要通过检查才会触发逻辑</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>entity_action</b> ({@code EntityAction}, 必选): 实体死亡时执行的操作</li>
 *   <li><b>attacker_condition</b> ({@code EntityCondition}, 可选): 检查攻击者实体的条件，默认允许任何攻击者</li>
 *   <li><b>damage_condition</b> ({@code DamageCondition}, 可选): 检查伤害来源的条件，默认允许任何伤害来源</li>
 * </ul>
 *
 * @see LivingEntityMixin 实际触发逻辑的Mixin类
 */
public class ActionOnDeathPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("attacker_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null);

    private final Consumer<LivingEntity> entity_action;
    private final Predicate<LivingEntity> attacker_condition;
    private final Predicate<Pair<DamageSource, Float>> damage_conidtion;

    public ActionOnDeathPower(PowerType<?> type, LivingEntity entity, Consumer<LivingEntity> entityAction, Predicate<LivingEntity> attackerCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(type, entity);
        entity_action = entityAction;
        attacker_condition = attackerCondition;
        damage_conidtion = damageCondition;
    }

    public boolean shouldApply(DamageSource source, float amount, LivingEntity attacker) {
        boolean damageValid = damage_conidtion == null || damage_conidtion.test(new Pair<>(source, amount));
        boolean attackerValid = attacker_condition == null ||
                (attacker_condition != null && attacker != null && attacker_condition.test(attacker));

        // 如果未设置attacker_condition，则只检查damage_condition
        return attacker_condition == null ? damageValid : (damageValid && attackerValid);
    }

    public void apply() {
        if (entity_action != null) {
            entity_action.accept(entity);
        }
    }
}
