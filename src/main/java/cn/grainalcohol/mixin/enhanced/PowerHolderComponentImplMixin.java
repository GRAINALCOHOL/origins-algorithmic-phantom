package cn.grainalcohol.mixin.enhanced;

import cn.grainalcohol.api.IPowerSoundControl;
import cn.grainalcohol.config.ModConfig;
import cn.grainalcohol.init.OAPSoundEvent;
import io.github.apace100.apoli.component.PowerHolderComponentImpl;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowerHolderComponentImpl.class)
public class PowerHolderComponentImplMixin implements IPowerSoundControl {
    @Shadow @Final private LivingEntity owner;

    @Unique private final boolean revokeCanPlay = ModConfig.getInstance().enhanced.revokePowerSoundEffect;

    @Unique private final float revokeVolume = ModConfig.getInstance().enhanced.revokePowerVolume;

    @Unique private static final int REVOKE_COOLDOWN = ModConfig.getInstance().enhanced.revokePowerSoundEffectCooldown;
    @Unique private int revokeLastTick = -REVOKE_COOLDOWN; // 确保第一次可以播放

    @Unique private boolean shouldPlay = true;

    @Inject(method = "removeAllPowersFromSource", at = @At("HEAD"))
    public void onStart(Identifier source, CallbackInfoReturnable<Integer> cir) {
        shouldPlay = false;
    }

    @Inject(method = "removeAllPowersFromSource", at = @At("RETURN"))
    public void onEnd(Identifier source, CallbackInfoReturnable<Integer> cir) {
        shouldPlay = true;
    }

    @Inject(method = "removePower", at = @At("TAIL"))
    private void onRemove(PowerType<?> powerType, Identifier source, CallbackInfo ci) {
        if (revokeCanPlay && !owner.getWorld().isClient() && shouldPlay) {
            int currentTick = owner.age;
            if (currentTick - revokeLastTick >= REVOKE_COOLDOWN) {
                revokeLastTick = currentTick;
                owner.getWorld().playSoundFromEntity(
                        null, owner,
                        OAPSoundEvent.REVOKE,
                        SoundCategory.PLAYERS,
                        revokeVolume, 1f
                );
            }
        }
    }

    @Unique private final boolean grantCanPlay = ModConfig.getInstance().enhanced.grantPowerSoundEffect;

    @Unique private final float grantVolume = ModConfig.getInstance().enhanced.grantPowerVolume;

    @Unique private static final int GRANT_COOLDOWN = ModConfig.getInstance().enhanced.grantPowerSoundEffectCooldown;
    @Unique private int grantLastTick = -GRANT_COOLDOWN; // 确保第一次可以播放

    @Unique private boolean isFromOrigin;

    @Inject(method = "addPower", at = @At("TAIL"))
    public void addPower(PowerType<?> powerType, Identifier source, CallbackInfoReturnable<Boolean> cir) {
        if (grantCanPlay && !owner.getWorld().isClient() && !isFromOrigin) {
            int currentTick = owner.age;
            if (currentTick - grantLastTick >= GRANT_COOLDOWN) {
                grantLastTick = currentTick;
                owner.getWorld().playSoundFromEntity(
                        null, owner,
                        OAPSoundEvent.GRANT,
                        SoundCategory.PLAYERS,
                        grantVolume, 1f
                );
            }
        }
    }

    @Override
    public void oap$setFromOrigin(boolean fromOrigin) {
        isFromOrigin = fromOrigin;
    }

    @Override
    public boolean oap$isFromOrigin() {
        return isFromOrigin;
    }
}
