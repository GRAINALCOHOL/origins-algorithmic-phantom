package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.action.entity.ModifyEffectAmplifierAction;
import cn.grainalcohol.action.entity.ModifyEffectDurationAction;
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
    }

    public static final ActionFactory<Entity> MODIFY_EFFECT_DURATION =
            new ActionFactory<>(
                    OAPMod.id("modify_effect_duration"),
                    ModifyEffectDurationAction.DATA,
                    new ModifyEffectDurationAction()
            );
    public static final ActionFactory<Entity> MODIFY_EFFECT_AMPLIFIER =
            new ActionFactory<>(OAPMod.id("modify_effect_amplifier"),
                    ModifyEffectAmplifierAction.DATA,
                    new ModifyEffectAmplifierAction()
            );
}
