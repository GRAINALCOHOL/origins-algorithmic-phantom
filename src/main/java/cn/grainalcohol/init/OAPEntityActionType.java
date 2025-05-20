package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.action.entity.ModifyEffectAmplifierAction;
import cn.grainalcohol.action.entity.ModifyEffectDurationAction;
import cn.grainalcohol.action.entity.ToggleCountdownAction;
import cn.grainalcohol.condition.entity.GiveAbsorptionHeartsAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class OAPEntityActionType {
    public static void init() {
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                MODIFY_EFFECT_DURATION.getSerializerId(),
                MODIFY_EFFECT_DURATION
        );
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                MODIFY_EFFECT_AMPLIFIER.getSerializerId(),
                MODIFY_EFFECT_AMPLIFIER
        );
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                TOGGLE_COUNTDOWN.getSerializerId(),
                TOGGLE_COUNTDOWN
        );
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                GIVE_ABSORPTION.getSerializerId(),
                GIVE_ABSORPTION
        );
    }

    public static final ActionFactory<Entity> MODIFY_EFFECT_DURATION =
            new ActionFactory<>(
                    OAPMod.id("modify_effect_duration"),
                    ModifyEffectDurationAction.DATA,
                    new ModifyEffectDurationAction()
            );
    public static final ActionFactory<Entity> MODIFY_EFFECT_AMPLIFIER =
            new ActionFactory<>(
                    OAPMod.id("modify_effect_amplifier"),
                    ModifyEffectAmplifierAction.DATA,
                    new ModifyEffectAmplifierAction()
            );
    public static final ActionFactory<Entity> TOGGLE_COUNTDOWN =
            new ActionFactory<>(
                    OAPMod.id("toggle_countdown"),
                    ToggleCountdownAction.DATA,
                    new ToggleCountdownAction()
            );
    public static final ActionFactory<Entity> GIVE_ABSORPTION =
            new ActionFactory<>(
                    OAPMod.id("give_absorption"),
                    GiveAbsorptionHeartsAction.DATA,
                    new GiveAbsorptionHeartsAction()
            );
}
