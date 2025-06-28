package cn.grainalcohol.power;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

public class DamageReflectionPercentPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("mode", SerializableDataTypes.STRING, "scale")
            .add("amount", SerializableDataTypes.FLOAT, 1f)
            .add("check_source", SerializableDataTypes.BOOLEAN, true)
            .add("random_addition", SerializableDataTypes.FLOAT, 0f)
            ;

    private final String mode;
    private final float amount;
    private final boolean check_source;
    private final float random_addition;

    public DamageReflectionPercentPower(PowerType<?> type, LivingEntity entity, String mode, float amount, boolean checkSource, float randomAddition) {
        super(type, entity);
        this.mode = mode;
        this.amount = amount;
        check_source = checkSource;
        random_addition = randomAddition;
    }

    public float getModifier() {
        return switch (mode) {
            case "scale" -> MathUtil.nonNegative(amount);
            case "multiply" -> (1 + MathUtil.nonNegative(amount));
            default -> 1f;
        };
    }

    public boolean isCheckSource() {
        return check_source;
    }

    public float getRandomAddition() {
        return (float) MathUtil.randomInRange(random_addition);
    }
}
