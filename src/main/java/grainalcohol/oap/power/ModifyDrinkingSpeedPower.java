package grainalcohol.oap.power;

import grainalcohol.oap.OAPMod;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

public class ModifyDrinkingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("mode", SerializableDataTypes.STRING, "scale")
            .add("amount", SerializableDataTypes.FLOAT, 1f);

    private final String mode;
    private final float amount;

    public ModifyDrinkingSpeedPower(PowerType<?> type, LivingEntity entity, String mode, float amount) {
        super(type, entity);
        this.mode = mode;
        this.amount = Math.min(10f, amount);
    }

    public float apply(float originalSpeed) {
        return switch (mode) {
            case "scale" -> originalSpeed * Math.max(0.1f, amount);
            case "multiply" -> originalSpeed * Math.max(0.1f, (1 + amount));
            default -> {
                OAPMod.LOGGER.warn("Unknown mode '{}' for {}", mode, getClass().getSimpleName());
                yield originalSpeed;
            }
        };
    }
}
