package cn.grainalcohol.power;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

public class ModifyDrinkingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("modifier", SerializableDataTypes.FLOAT, 1.0f);

    private final float Modifier;

    public ModifyDrinkingSpeedPower(PowerType<?> type, LivingEntity entity, float modifier) {
        super(type, entity);
        Modifier = MathUtil.clamp(0.1f, 10f, modifier);
    }

    public float apply(float originalSpeed) {
        return originalSpeed * Modifier;
    }
}
