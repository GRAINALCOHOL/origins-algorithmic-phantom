package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.effect.ChokeEffect;
import cn.grainalcohol.effect.FrozenEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OAPStatusEffect {
    public static void init() {
        Registry.register(Registries.STATUS_EFFECT,
                new Identifier(OAPMod.MOD_ID, "frozen"),
                new FrozenEffect()
        );
        Registry.register(Registries.STATUS_EFFECT,
                new Identifier(OAPMod.MOD_ID,"choke"),
                new ChokeEffect()
        );
    }
}
