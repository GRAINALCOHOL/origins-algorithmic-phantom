package cn.grainalcohol.condition.entity;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;

/**
 * 类型ID: oap:attack_cooldown<br>
 * <br>
 * 检测玩家的攻击冷却进度
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>comparison</b> ({@code Comparison}, 必选): 将决定如何与目标值进行比较</li>
 *   <li><b>compare_to</b> ({@code float}, 必选): 用于比较的目标值，将被自动修正至0~1之间</li>
 * </ul>
 */
public class AttackCooldownCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        float cooldownProgress = player.getAttackCooldownProgress(0.5f);

        Comparison comparison = data.get("comparison");
        float compareTo = MathUtil.clamp(0f, 1f, data.getFloat("compare_to"));
        return comparison.compare(cooldownProgress, compareTo);
    }
}
