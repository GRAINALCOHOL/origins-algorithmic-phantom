package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.condition.bientity.IsTeamMemberCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class OAPBientityConditionType {
    public static void init() {
        registerBientityCondition(OAPMod.id("is_team_member"), IsTeamMemberCondition.DATA, new IsTeamMemberCondition());
    }

    private static void registerBientityCondition(Identifier conditionId, SerializableData data, BiFunction<SerializableData.Instance, Pair<Entity, Entity>, Boolean> condition) {
        Registry.register(
                ApoliRegistries.BIENTITY_CONDITION, conditionId,
                new ConditionFactory<>(
                    conditionId, data, condition
                )
        );
    }
}
