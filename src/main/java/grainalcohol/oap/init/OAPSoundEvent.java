package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class OAPSoundEvent {
    public static final SoundEvent GRANT = register(OAPMod.id("grant"));
    public static final SoundEvent REVOKE = register(OAPMod.id("revoke"));

    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        OAPMod.LOGGER.info("Registering Mod Items for" + OAPMod.MOD_ID);
    }
}
