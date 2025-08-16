package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.action.entity.SayAction;
import grainalcohol.oap.action.entity.*;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

import static com.mojang.text2speech.Narrator.LOGGER;

public class OAPEntityActionType {
    public static void init() {
        registerAction(OAPMod.id("modify_effect_duration"), ModifyEffectDurationAction.DATA, new ModifyEffectDurationAction());
        registerAction(OAPMod.id("modify_effect_amplifier"), ModifyEffectAmplifierAction.DATA, new ModifyEffectAmplifierAction());
        registerAction(OAPMod.id("toggle_countdown"), ToggleCountdownAction.DATA, new ToggleCountdownAction());
        if (FabricLoader.getInstance().isModLoaded("immersivemessages")) {
            registerAction(OAPMod.id("send_a_message"), SendImmersiveMessage.DATA, new SendImmersiveMessage());
        }else {
            LOGGER.info("Immersive Messages API not found");
        }
        registerAction(OAPMod.id("summon_tamed"), SummonTamedAction.DATA, new SummonTamedAction());
        registerAction(OAPMod.id("apply_random_status_effect"), ApplyRandomStatusEffectAction.DATA, new ApplyRandomStatusEffectAction());
        registerAction(OAPMod.id("modify_origin"), ModifyOriginAction.DATA, new ModifyOriginAction());
        registerAction(OAPMod.id("say"), SayAction.DATA, new SayAction());
        registerAction(OAPMod.id("toast"), ToastAction.DATA, new ToastAction());
        registerAction(OAPMod.id("modify_absorption"), ModifyAbsorptionAction.DATA, new ModifyAbsorptionAction());
        registerAction(OAPMod.id("knock_up"), KnockUpAction.DATA, new KnockUpAction());
        registerAction(OAPMod.id("debug"), DebugAction.DATA, new DebugAction());
        registerAction(OAPMod.id("to_other_player"), ToOtherPlayerAction.DATA, new ToOtherPlayerAction());
        registerAction(OAPMod.id("to_block"), ToBlockAction.DATA, new ToBlockAction());
    }

    private static void registerAction(Identifier actionId, SerializableData data, BiConsumer<SerializableData.Instance, Entity> action) {
        Registry.register(
                ApoliRegistries.ENTITY_ACTION, actionId,
                new ActionFactory<>(
                        actionId, data, action
                )
        );
    }
}
