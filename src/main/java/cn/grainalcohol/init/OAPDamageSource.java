package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class OAPDamageSource {
    public static final RegistryKey<DamageType> CHOKING =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
                    new Identifier(OAPMod.MOD_ID, "choking")
            );
}
