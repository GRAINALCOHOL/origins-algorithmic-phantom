package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.condition.entity.*;
import cn.grainalcohol.condition.entity.enhanced.EnhancedStatusEffectCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class OAPEntityConditionType {
    public static void init(){
        registerEntityCondition(OAPMod.id("game_day"), GameDayCondition.DATA, new GameDayCondition());
        registerEntityCondition(OAPMod.id("attack_cooldown"), AttackCooldownCondition.DATA, new AttackCooldownCondition());
        registerEntityCondition(OAPMod.id("status_effect"), EnhancedStatusEffectCondition.DATA, new EnhancedStatusEffectCondition());
        registerEntityCondition(OAPMod.id("countdown_progress"), CountdownProgressCondition.DATA, new CountdownProgressCondition());
        registerEntityCondition(OAPMod.id("countdown_is_active"), CountdownIsActiveCondition.DATA, new CountdownIsActiveCondition());
        registerEntityCondition(OAPMod.id("number_of_player"), NumberOfPlayerCondition.DATA, new NumberOfPlayerCondition());
        registerEntityCondition(OAPMod.id("light_level"), LightLevelCondition.DATA, new LightLevelCondition());
        registerEntityCondition(OAPMod.id("random"), RandomCondition.DATA, new RandomCondition());
        registerEntityCondition(OAPMod.id("is_friendly"), IsFriendlyCondition.DATA, new IsFriendlyCondition());
    }

    private static void registerEntityCondition(Identifier conditionId, SerializableData data, BiFunction<SerializableData.Instance, Entity, Boolean> condition) {
        Registry.register(
                ApoliRegistries.ENTITY_CONDITION, conditionId,
                new ConditionFactory<>(
                        conditionId, data, condition
                )
        );
    }
}
