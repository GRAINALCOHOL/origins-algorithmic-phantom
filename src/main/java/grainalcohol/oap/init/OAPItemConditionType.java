package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.api.PityDataHolder;
import grainalcohol.oap.util.MiscUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class OAPItemConditionType {
    public static void init() {

    }

    private static void registerItemCondition(Identifier conditionId, SerializableData data, BiFunction<SerializableData.Instance, ItemStack, Boolean> condition) {
        Registry.register(
                ApoliRegistries.ITEM_CONDITION, conditionId,
                new ConditionFactory<>(
                        conditionId, data, condition
                )
        );
    }
}
