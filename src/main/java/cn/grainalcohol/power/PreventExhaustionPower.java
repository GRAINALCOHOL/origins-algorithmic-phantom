package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

public class PreventExhaustionPower extends Power {
    public static final SerializableData DATA = new SerializableData();

    public PreventExhaustionPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
}
