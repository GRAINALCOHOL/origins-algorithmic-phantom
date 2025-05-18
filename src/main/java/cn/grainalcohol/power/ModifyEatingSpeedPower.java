package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ModifyEatingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("modifier", SerializableDataTypes.FLOAT, 1.0f);

    private final float MODIFIER;

    public ModifyEatingSpeedPower(PowerType<?> type, LivingEntity entity, float modifier) {
        super(type, entity);
        if (!(entity instanceof PlayerEntity)) {
            throw new IllegalArgumentException("ModifyEatingSpeedPower can only be applied to players");
        }
        this.MODIFIER = Math.max(0.1f, modifier);
    }

    public float modifyEatingSpeed(float originalSpeed) {
        return originalSpeed * MODIFIER;
    }
}
