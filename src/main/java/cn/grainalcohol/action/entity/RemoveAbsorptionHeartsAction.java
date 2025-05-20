package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.BiConsumer;

public class RemoveAbsorptionHeartsAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("amount", SerializableDataTypes.FLOAT);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        float amount = data.getFloat("amount");
        float currentAbsorption = livingEntity.getAbsorptionAmount();

        //确保不低于0
        float newAbsorption = Math.max(0, currentAbsorption - amount);
        livingEntity.setAbsorptionAmount(newAbsorption);
    }
}