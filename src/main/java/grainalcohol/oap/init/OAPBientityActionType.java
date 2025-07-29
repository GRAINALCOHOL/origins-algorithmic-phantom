package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.action.bientity.DamageByAttributeAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiConsumer;

public class OAPBientityActionType {
    public static void init(){
        registerBientityAction(OAPMod.id("damage_by_attribute"), DamageByAttributeAction.DATA, new DamageByAttributeAction());
    }

    private static void registerBientityAction(Identifier actionId, SerializableData data, BiConsumer<SerializableData.Instance, Pair<Entity, Entity>> action) {
        Registry.register(
                ApoliRegistries.BIENTITY_ACTION, actionId,
                new ActionFactory<>(
                        actionId, data, action
                )
        );
    }
}
