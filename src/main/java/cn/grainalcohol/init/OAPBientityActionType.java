package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.action.bientity.DamageByAttributeAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class OAPBientityActionType {
    public static void init(){
        Registry.register(ApoliRegistries.BIENTITY_ACTION,
                DAMAGE_BY_ATTRIBUTE.getSerializerId(),
                DAMAGE_BY_ATTRIBUTE
        );
    }

    public static final ActionFactory<Pair<Entity, Entity>> DAMAGE_BY_ATTRIBUTE =
            new ActionFactory<>(
                    OAPMod.id("damage_by_attribute"),
                    DamageByAttributeAction.DATA,
                    new DamageByAttributeAction()
            );
}
