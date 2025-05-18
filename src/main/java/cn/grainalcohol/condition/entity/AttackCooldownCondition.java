package cn.grainalcohol.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;

public class AttackCooldownCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT)
            .add("invert", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        float cooldownProgress = player.getAttackCooldownProgress(0.5f);

        Comparison comparison = data.get("comparison");
        float compareTo = data.getFloat("compare_to");
        boolean result = comparison.compare(cooldownProgress, compareTo);
        return data.getBoolean("invert") ? !result : result;
    }
}
