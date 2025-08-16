package grainalcohol.oap.condition.entity;

import grainalcohol.oap.api.PityDataHolder;
import grainalcohol.oap.util.MathUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;

import java.util.function.BiFunction;

public class RandomCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("chance", SerializableDataTypes.FLOAT, 1f)
            .add("at_max", SerializableDataTypes.BOOLEAN, false)
            .add("allow_pity", SerializableDataTypes.BOOLEAN, false)
            .add("extra_pity_count", SerializableDataTypes.INT, 0)
            .add("pool_id", SerializableDataTypes.STRING, "default_pool")
            ;

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient() || !(entity instanceof PityDataHolder pityDataHolder)) {
            return false;
        }

        float chance = data.getFloat("chance");
        boolean allowPity = data.getBoolean("allow_pity");
        int maximum = (int) (1f / chance) + data.getInt("extra_pity_count");
        String poolId = data.getString("pool_id");

        if (data.getBoolean("at_max")) maximum -= 1;

        boolean success = MathUtil.randomChance(chance);

        if (!allowPity) {
            return success;
        }

        int pityCount = pityDataHolder.oap$getPityCount(poolId);

        if (success || pityCount >= maximum) {
            pityDataHolder.oap$resetPity(poolId);
            return true;
        } else {
            pityDataHolder.oap$incrementPity(poolId);
            return false;
        }
    }
}
