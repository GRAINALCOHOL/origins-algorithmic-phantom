package grainalcohol.oap.condition.entity;

import grainalcohol.oap.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;

// 这只能检查指定实体算不算没有还手能力的被动生物
public class IsFriendlyCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("include_neutral", SerializableDataTypes.BOOLEAN, false)
            .add("player_friendly", SerializableDataTypes.BOOLEAN, true);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        boolean includeNeutral = data.getBoolean("include_neutral");

        if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity livingEntity) {
            return EntityUtil.isFriendly(livingEntity, includeNeutral,
                    true, data.getBoolean("player_friendly")
            );
        }

        return false;
    }
}
