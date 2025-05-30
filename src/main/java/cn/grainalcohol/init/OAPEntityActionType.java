package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.action.entity.*;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

import static com.mojang.text2speech.Narrator.LOGGER;

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
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                REMOVE_ABSORPTION.getSerializerId(),
                REMOVE_ABSORPTION
        );
        if (FabricLoader.getInstance().isModLoaded("immersivemessages")) {
            Registry.register(ApoliRegistries.ENTITY_ACTION,
                    SEND_A_MESSAGE.getSerializerId(),
                    SEND_A_MESSAGE
            );
        }else {
            LOGGER.info("Immersive Messages API not found");
        }
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                SUMMON_TAMED.getSerializerId(),
                SUMMON_TAMED
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
    public static final ActionFactory<Entity> REMOVE_ABSORPTION =
            new ActionFactory<>(
                    OAPMod.id("remove_absorption"),
                    RemoveAbsorptionHeartsAction.DATA,
                    new RemoveAbsorptionHeartsAction()
            );
    public static final ActionFactory<Entity> SEND_A_MESSAGE =
            new ActionFactory<>(
                    OAPMod.id("send_a_message"),
                    SendImmersiveMessage.DATA,
                    new SendImmersiveMessage()
            );
    public static final ActionFactory<Entity> SUMMON_TAMED =
            new ActionFactory<>(
                    OAPMod.id("summon_tamed"),
                    SummonTamedAction.DATA,
                    new SummonTamedAction()
            );
}
