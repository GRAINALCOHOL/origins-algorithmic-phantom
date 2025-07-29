package grainalcohol.oap.condition.entity;

import grainalcohol.oap.config.OAPConfig;
import grainalcohol.oap.config.PityConfig;
import grainalcohol.oap.util.MathUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

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
        if (entity.getWorld().isClient()){
            return false;
        }

        float chance = data.getFloat("chance");
        boolean allowPity = data.getBoolean("allow_pity");
        int maximum = (int) (1f / chance) + data.getInt("extra_pity_count");

        if (data.getBoolean("at_max")) maximum -= 1;

        boolean success = MathUtil.randomChance(chance);

        if (!allowPity) {
            return success;
        }

        String worldName = null;
        String entityUuid = entity.getUuid().toString();
        String poolId = data.getString("pool_id");
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            worldName = serverWorld.getServer().getSaveProperties().getLevelName();
        }

        // 获取配置信息
        PityConfig pityConfig = OAPConfig.getInstance().getPityConfig();
        int pityCount = pityConfig.getPityCount(worldName, entityUuid, poolId);

        // 检查是否触发保底
        if (success || pityCount >= maximum) {
            pityConfig.resetPity(worldName, entityUuid, poolId);
            return true;
        } else {
            pityConfig.incrementPity(worldName, entityUuid, poolId);
            return false;
        }
    }

}
