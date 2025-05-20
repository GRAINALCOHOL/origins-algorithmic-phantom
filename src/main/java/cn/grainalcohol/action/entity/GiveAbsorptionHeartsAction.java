package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.BiConsumer;

public class GiveAbsorptionHeartsAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("amount", SerializableDataTypes.FLOAT);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        float amount = data.getFloat("amount");
        float currentAbsorption = livingEntity.getAbsorptionAmount();

        livingEntity.setAbsorptionAmount(Math.min(currentAbsorption + amount, Float.MAX_VALUE));
    }
}
