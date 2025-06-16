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

import java.util.ArrayList;
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
                COUNTDOWN.getSerializerId(),
                COUNTDOWN
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                HIDE_STATUS_BARS.getSerializerId(),
                HIDE_STATUS_BARS
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                ACTION_ON_DEATH.getSerializerId(),
                ACTION_ON_DEATH
        );
        Registry.register(
                ApoliRegistries.POWER_FACTORY,
                ADVANCEMENT_PROGRESS.getSerializerId(),
                ADVANCEMENT_PROGRESS
        );
    }



    public static final PowerFactory<?> ACTION_ON_EFFECT_GAINED =
            new PowerFactory<>(
                    OAPMod.id("action_on_effect_gained"),
                    ActionOnEffectGainedPower.DATA,
                    (data) -> (type, entity) -> new ActionOnEffectGainedPower(
                            type,
                            entity,
                            data.isPresent("condition") ? data.get("condition") : null,
                            data.get("entity_action"),
                            data.isPresent("effect") ? data.getId("effect") : null,
                            data.isPresent("effects") ? data.get("effects") : new ArrayList<>(),
                            data.getBoolean("include_update")
                    )
            ).allowCondition();

    public static final PowerFactory<?> MODIFY_EATING_SPEED =
            new PowerFactory<>(
                    OAPMod.id("modify_eating_speed"),
                    ModifyEatingSpeedPower.DATA,
                    (data) -> (type, entity) -> new ModifyEatingSpeedPower(
                            type, entity,
                            data.getFloat("modifier"),
                            data.isPresent("affects_potions") ? data.getBoolean("affects_potions") : false
                    )
            ).allowCondition();

    public static final PowerFactory<?> MODIFY_MOB_BEHAVIOR =
            new PowerFactory<>(
                    OAPMod.id("modify_mob_behavior"),
                    ModifyMobBehaviorPower.DATA,
                    (data) -> (type, entity) -> new ModifyMobBehaviorPower(
                            type, entity,
                            data.isPresent("entity_condition") ? data.get("entity_condition") : null,
                            data.getString("behavior")
                    )
            ).allowCondition();

    public static final PowerFactory<?> PREVENT_EXHAUSTION =
            new PowerFactory<Power>(
                    OAPMod.id("prevent_exhaustion"),
                    PreventExhaustionPower.DATA,
                    (data) -> PreventExhaustionPower::new
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

    public static final PowerFactory<?> HIDE_STATUS_BARS =
            new PowerFactory<>(
                    OAPMod.id("hide_status_bars"),
                    HideStatusBarsPower.DATA,
                    (data) -> (type, entity) -> new HideStatusBarsPower(type, entity)
            ).allowCondition();

    public static final PowerFactory<?> ACTION_ON_DEATH =
            new PowerFactory<>(
                    OAPMod.id("action_on_death"),
                    ActionOnDeathPower.DATA,
                    (data) -> (type, entity) -> new ActionOnDeathPower(
                            type, entity,
                            data.get("entity_action"),
                            data.get("attacker_condition"),
                            data.get("damage_condition")
                    )
            ).allowCondition();

    public static final PowerFactory<?> ADVANCEMENT_PROGRESS =
            new PowerFactory<>(
                    OAPMod.id("advancement_progress"),
                    AdvancementProgressPower.DATA,
                    (data) -> (type, entity) -> new AdvancementProgressPower(
                            type, entity,
                            data.getId("advancement"),
                            data.get("hud_render"),
                            data.isPresent("on_complete") ? data.get("on_complete") : null
                    )
            ).allowCondition();
}
