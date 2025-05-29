package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class OAPDamageSource {
    public static final RegistryKey<DamageType> CHOKING =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
                    OAPMod.id("choking")
            );

    public static final RegistryKey<DamageType> TRUE_DAMAGE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
                    OAPMod.id("true_damage")
            );
}
