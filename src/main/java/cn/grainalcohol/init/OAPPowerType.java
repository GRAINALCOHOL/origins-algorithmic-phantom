package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.power.*;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OAPPowerType {
    public static void init() {
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                ACTION_ON_EFFECT_GAINED.getSerializerId(),
                ACTION_ON_EFFECT_GAINED
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                MODIFY_EATING_SPEED.getSerializerId(),
                MODIFY_EATING_SPEED
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                MODIFY_MOB_BEHAVIOR.getSerializerId(),
                MODIFY_MOB_BEHAVIOR
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                PREVENT_EXHAUSTION.getSerializerId(),
                PREVENT_EXHAUSTION
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                ACTION_ON_SLEEP_COMPLETE.getSerializerId(),
                ACTION_ON_SLEEP_COMPLETE
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                COUNTDOWN.getSerializerId(),
                COUNTDOWN
        );
    }

    public static final PowerFactory<?> ACTION_ON_EFFECT_GAINED =
            new PowerFactory<>(
                    OAPMod.id("action_on_effect_gained"),
                    ActionOnEffectGainedPower.DATA,
                    (data) -> (type, entity) -> {
                        Set<StatusEffect> effects = new HashSet<>();
                        // 处理单个effect
                        if(data.isPresent("effect")) {
                            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(data.getString("effect")));
                            if(effect != null) effects.add(effect);
                        }
                        // 处理多个effects
                        if(data.isPresent("effects")) {
                            List<String> effectIds = data.get("effects");
                            effectIds.forEach(id -> {
                                StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(id));
                                if(effect != null) effects.add(effect);
                            });
                        }
                        return new ActionOnEffectGainedPower(
                                type,
                                entity,
                                data.isPresent("condition") ? data.get("condition") : null,
                                data.get("entity_action"),
                                effects,
                                data.getBoolean("include_update")
                        );
                    }
            ).allowCondition();

    public static final PowerFactory<?> MODIFY_EATING_SPEED =
            new PowerFactory<>(
                    OAPMod.id("modify_eating_speed"),
                    ModifyEatingSpeedPower.DATA,
                    (data) -> (type, entity) -> new ModifyEatingSpeedPower(
                            type, entity,
                            data.getFloat("modifier")
                    )
            ).allowCondition();

    public static final PowerFactory<?> MODIFY_MOB_BEHAVIOR =
            new PowerFactory<>(
                    OAPMod.id("modify_mob_behavior"),
                    ModifyMobBehaviorPower.DATA,
                    (data) -> (type, entity) -> new ModifyMobBehaviorPower(
                            type, entity,
                            data.isPresent("entity_condition") ? data.get("entity_condition") : null,
                            ModifyMobBehaviorPower.EntityBehavior.valueOf(data.getString("behavior").toUpperCase())
                    )
            ).allowCondition();

    public static final PowerFactory<?> PREVENT_EXHAUSTION =
            new PowerFactory<Power>(
                    OAPMod.id("prevent_exhaustion"),
                    PreventExhaustionPower.DATA,
                    (data) -> PreventExhaustionPower::new
            ).allowCondition();

    public static final PowerFactory<?> ACTION_ON_SLEEP_COMPLETE =
            new PowerFactory<>(
                    OAPMod.id("action_on_sleep_complete"),
                    ActionOnSleepCompletePower.DATA,
                    (data) -> (type, entity) -> new ActionOnSleepCompletePower(
                            type, entity,
                            data.isPresent("condition") ? data.get("condition") : null,
                            data.get("entity_action")
                    )
            ).allowCondition();
    public static final PowerFactory<?> COUNTDOWN =
            new PowerFactory<>(
                    OAPMod.id("countdown"),
                    CountdownPower.DATA,
                    (data) -> (type, entity) -> new CountdownPower(
                            type, entity,
                            data.getInt("countdown"),
                            data.isPresent("immediately_start") ? data.getBoolean("immediately_start") : false,
                            data.get("ending_action"),
                            data.isPresent("per_time_action") ? data.get("per_time_action") : null,
                            data.isPresent("action_interval") ? data.getInt("action_interval") : 20,
                            data.isPresent("condition") ? data.get("condition") : null,
                            data.isPresent("hud_render") ? data.get("hud_render") : null
                    )
            ).allowCondition();
}
