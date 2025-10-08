package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.api.PityDataHolder;
import grainalcohol.oap.condition.entity.*;
import grainalcohol.oap.condition.entity.enhanced.EnhancedStatusEffectCondition;
import grainalcohol.oap.util.MiscUtil;
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
        registerEntityCondition(OAPMod.id("random"), MiscUtil.RANDOM_CONDITION_DATA, (data, entity) -> {
            if (entity.getWorld().isClient() || !(entity instanceof PityDataHolder pityDataHolder)) {
                return false;
            }

            // 建议使用在能力自带的条件检查，也就是这里的oap:random
            // 属于“entity condition type”，因为不同类的条件要分别注册
            // 不光配置麻烦，使用也麻烦，完全可以用更简单的方法（上文）替代
            return MiscUtil.handlePityData(data, pityDataHolder);
        });
        registerEntityCondition(OAPMod.id("is_friendly"), IsFriendlyCondition.DATA, new IsFriendlyCondition());
        registerEntityCondition(OAPMod.id("name"), NameCondition.DATA, new NameCondition());
        registerEntityCondition(OAPMod.id("countdown_is_finished"), CountdownIsFinishedCondition.DATA, new CountdownIsFinishedCondition());
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
