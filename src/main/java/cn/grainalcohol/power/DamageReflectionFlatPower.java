package cn.grainalcohol.power;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

public class DamageReflectionFlatPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("amount", SerializableDataTypes.FLOAT, 1f)
            .add("chance", SerializableDataTypes.FLOAT, 1f)
            .add("check_source", SerializableDataTypes.BOOLEAN, true)
            .add("random_addition", SerializableDataTypes.FLOAT, 0f)
            ;

    private final float amount;
    private final float chance;
    private final boolean check_source;
    private final float random_addition;

    public DamageReflectionFlatPower(PowerType<?> type, LivingEntity entity, float amount, float chance, boolean checkSource, float randomAddition) {
        super(type, entity);
        this.amount = amount;
        this.chance = chance;
        check_source = checkSource;
        random_addition = randomAddition;
    }

    public float getAmount() {
        return MathUtil.clamp(Float.MIN_VALUE, Float.MIN_VALUE, amount);
    }

    public boolean isCheckSource() {
        return check_source;
    }

    public float getChance() {
        return chance;
    }

    public float getRandomAddition() {
        return (float) MathUtil.randomInRange(random_addition);
    }
}
