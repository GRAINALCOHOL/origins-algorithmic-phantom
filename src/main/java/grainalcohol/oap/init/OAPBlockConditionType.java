package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.api.PityDataHolder;
import grainalcohol.oap.util.MiscUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class OAPBlockConditionType {
    public static void init() {
        registerBlockCondition(OAPMod.id("random"), MiscUtil.RANDOM_CONDITION_DATA, (data, blockPosition) -> {
            if (blockPosition.getWorld().isClient() || !(blockPosition.getBlockEntity() instanceof PityDataHolder pityDataHolder)) {
                return false;
            }

            return MiscUtil.handlePityData(data, pityDataHolder);
        });
    }

    private static void registerBlockCondition(Identifier conditionId, SerializableData data, BiFunction<SerializableData.Instance, CachedBlockPosition, Boolean> condition) {
        Registry.register(
                ApoliRegistries.BLOCK_CONDITION, conditionId,
                new ConditionFactory<>(
                        conditionId, data, condition
                )
        );
    }
}
