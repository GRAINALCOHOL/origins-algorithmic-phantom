package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.power.*;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class OAPPowerType {
    public static void init() {
        registerPower(
                OAPMod.id("action_on_effect_gained"),
                ActionOnEffectGainedPower.DATA,
                true,
                (data) -> (type, entity) -> new ActionOnEffectGainedPower(
                        type, entity,
                        data.get("entity_action"),
                        data.getId("effect"),
                        data.get("effects"),
                        data.getBoolean("include_update"),
                        data.getBoolean("check_all")
                )
        );
        registerPower(
                OAPMod.id("modify_eating_speed"),
                ModifyEatingSpeedPower.DATA,
                true,
                (data) -> (type, entity) -> new ModifyEatingSpeedPower(
                        type, entity,
                        data.getString("mode"),
                        data.getFloat("amount")
                )
        );
        registerPower(
                OAPMod.id("modify_drinking_speed"),
                ModifyDrinkingSpeedPower.DATA,
                true,
                (data) -> (type, entity) -> new ModifyDrinkingSpeedPower(
                        type, entity,
                        data.getString("mode"),
                        data.getFloat("amount")
                )
        );
        registerPower(
                OAPMod.id("modify_mob_behavior"),
                ModifyMobBehaviorPower.DATA,
                true,
                (data) -> (type, entity) -> new ModifyMobBehaviorPower(
                        type, entity,
                        data.get("entity_condition"),
                        data.getString("behavior")
                )
        );
        registerPower(
                OAPMod.id("prevent_exhaustion"),
                PreventExhaustionPower.DATA,
                true,
                (data) -> PreventExhaustionPower::new
        );
        registerPower(
                OAPMod.id("countdown"),
                CountdownPower.DATA,
                true,
                (data) -> (type, entity) -> new CountdownPower(
                        type, entity,
                        data.getInt("countdown"),
                        data.getBoolean("immediately_start"),
                        data.get("ending_action"),
                        data.get("per_time_action"),
                        data.getInt("action_interval"),
                        data.get("hud_render")
                )
        );
        registerPower(
                OAPMod.id("hide_status_bars"),
                HideStatusBarsPower.DATA,
                true,
                (data) -> HideStatusBarsPower::new
        );
        registerPower(
                OAPMod.id("action_on_death"),
                ActionOnDeathPower.DATA,
                true,
                (data) -> (type, entity) -> new ActionOnDeathPower(
                        type, entity,
                        data.get("entity_action"),
                        data.get("attacker_condition"),
                        data.get("damage_condition")
                )
        );
        registerPower(
                OAPMod.id("advancement_progress"),
                AdvancementProgressPower.DATA,
                true,
                (data) -> (type, entity) -> new AdvancementProgressPower(
                        type, entity,
                        data.getId("advancement"),
                        data.get("hud_render"),
                        data.get("on_complete")
                )
        );
        registerPower(
                OAPMod.id("prevent_movement_axis"),
                PreventMovementAxisPower.DATA,
                true,
                (data) -> (type, entity) -> new PreventMovementAxisPower(
                        type, entity,
                        data.getBoolean("prevent_positive_x"),
                        data.getBoolean("prevent_negative_x"),
                        data.getBoolean("prevent_positive_y"),
                        data.getBoolean("prevent_negative_y"),
                        data.getBoolean("prevent_positive_z"),
                        data.getBoolean("prevent_negative_z")
                )
        );
        registerPower(
                OAPMod.id("damage_reflection_flat"),
                DamageReflectionFlatPower.DATA,
                true,
                (data) -> (type, entity) -> new DamageReflectionFlatPower(
                        type, entity,
                        data.getString("mode"),
                        data.getFloat("amount"),
                        data.getBoolean("check_source"),
                        data.getFloat("random_addition")
                )
        );
        registerPower(
                OAPMod.id("damage_reflection_percent"),
                DamageReflectionPercentPower.DATA,
                true,
                (data) -> (type, entity) -> new DamageReflectionPercentPower(
                        type, entity,
                        data.getString("mode"),
                        data.getFloat("amount"),
                        data.getBoolean("check_source"),
                        data.getFloat("random_addition")
                )
        );
        registerPower(
                OAPMod.id("action_on_absorption_change"),
                ActionOnAbsorptionChangePower.DATA,
                true,
                (data) -> (type, entity) -> new ActionOnAbsorptionChangePower(
                        type, entity,
                        data.get("increase_action"),
                        data.get("decrease_action")
                )
        );
    }

    private static <P extends Power> void registerPower(Identifier powerId, SerializableData data, boolean allowCondition, Function<SerializableData.Instance, BiFunction<PowerType<P>, LivingEntity, P>> factory) {
        PowerFactory<P> powerFactory = new PowerFactory<>(powerId, data, factory);
        if (allowCondition) {
            powerFactory = powerFactory.allowCondition();
        }
        Registry.register(ApoliRegistries.POWER_FACTORY, powerId, powerFactory);
    }
}
