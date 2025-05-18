package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.condition.entity.AttackCooldownCondition;
import cn.grainalcohol.condition.entity.GameDayCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class OAPEntityConditionType {
    public static void init(){
        Registry.register(
                ApoliRegistries.ENTITY_CONDITION,
                GAME_DAY.getSerializerId(),
                GAME_DAY
        );
        Registry.register(
                ApoliRegistries.ENTITY_CONDITION,
                ATTACK_COOLDOWN.getSerializerId(),
                ATTACK_COOLDOWN
        );
    }

    public static final ConditionFactory<Entity> GAME_DAY =
            new ConditionFactory<>(
                    OAPMod.id("game_day"),
                    GameDayCondition.DATA,
                    new GameDayCondition()
            );
    public static final ConditionFactory<Entity> ATTACK_COOLDOWN =
            new ConditionFactory<>(
                    OAPMod.id("attack_cooldown"),
                    AttackCooldownCondition.DATA,
                    new AttackCooldownCondition()
            );
}
