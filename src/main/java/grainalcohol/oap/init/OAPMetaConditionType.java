package grainalcohol.oap.init;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class OAPMetaConditionType {
    private static void registerEntityCondition(SerializableDataTypes dataType, Identifier conditionId, SerializableData data, BiFunction<SerializableData.Instance, Entity, Boolean> condition) {
        Registry.register(
                ApoliRegistries.ENTITY_CONDITION, conditionId,
                new ConditionFactory<>(
                        conditionId, data, condition
                )
        );
    }
}
