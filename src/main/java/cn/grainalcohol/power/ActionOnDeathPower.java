package cn.grainalcohol.power;

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
 * 当实体死亡时触发指定动作的Power类型
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>entity_action</b> (entity action类型, 可选): 实体死亡时执行的动作</li>
 *   <li><b>attacker_condition</b> (entity condition类型, 可选): 检查攻击者实体的条件，默认允许任何攻击者</li>
 *   <li><b>damage_condition</b> (damage condition类型, 可选): 检查伤害来源的条件，默认允许任何伤害来源</li>
 * </ul>
 *
 * @see cn.grainalcohol.mixin.LivingEntityMixin 实际触发逻辑的Mixin类
 */
public class ActionOnDeathPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            .add("attacker_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null);

    private final Consumer<LivingEntity> ENTITY_ACTION;
    private final Predicate<LivingEntity> ATTACKER_CONDITION;
    private final Predicate<Pair<DamageSource, Float>> DAMAGE_CONDITION;

    public ActionOnDeathPower(PowerType<?> type, LivingEntity entity, Consumer<LivingEntity> entityAction, Predicate<LivingEntity> attackerCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(type, entity);
        ENTITY_ACTION = entityAction;
        ATTACKER_CONDITION = attackerCondition;
        DAMAGE_CONDITION = damageCondition;
    }

    public boolean shouldApply(DamageSource source, float amount, LivingEntity attacker) {
        boolean damageValid = DAMAGE_CONDITION == null || DAMAGE_CONDITION.test(new Pair<>(source, amount));
        boolean attackerValid = ATTACKER_CONDITION == null ||
                (ATTACKER_CONDITION != null && attacker != null && ATTACKER_CONDITION.test(attacker));

        System.out.println("[DEBUG] 伤害条件检查: " + damageValid);
        if(ATTACKER_CONDITION != null) {
            System.out.println("[DEBUG] 攻击者条件检查: " + attackerValid);
        }

        // 如果未设置attacker_condition，则只检查damage_condition
        return ATTACKER_CONDITION == null ? damageValid : (damageValid && attackerValid);
    }

    public void apply() {
        if (ENTITY_ACTION != null) {
            ENTITY_ACTION.accept(entity);
        }
    }
}
