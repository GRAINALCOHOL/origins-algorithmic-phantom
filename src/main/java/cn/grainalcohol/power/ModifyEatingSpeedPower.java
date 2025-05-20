package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

public class ModifyEatingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("modifier", SerializableDataTypes.FLOAT, 1.0f)
            .add("affects_potions", SerializableDataTypes.BOOLEAN, false);

    private final float MODIFIER;
    private final boolean AFFECTS_POTIONS;

    public ModifyEatingSpeedPower(PowerType<?> type, LivingEntity entity, float modifier, boolean affectsPotions) {
        super(type, entity);
        this.MODIFIER = Math.max(0.1f, modifier);
        this.AFFECTS_POTIONS = affectsPotions;
    }

    public boolean affectsPotions() {
        return AFFECTS_POTIONS;
    }

    public float modifyEatingSpeed(float originalSpeed) {
        return originalSpeed * MODIFIER;
    }
}
