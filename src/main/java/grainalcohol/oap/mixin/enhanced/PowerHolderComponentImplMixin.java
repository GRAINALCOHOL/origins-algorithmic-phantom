package grainalcohol.oap.mixin.enhanced;

import grainalcohol.oap.api.PowerSoundControl;
import grainalcohol.oap.config.CommonConfig;
import grainalcohol.oap.config.OAPConfig;
import grainalcohol.oap.init.OAPSoundEvent;
import grainalcohol.oap.network.SoundPacket;
import io.github.apace100.apoli.component.PowerHolderComponentImpl;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
public class PowerHolderComponentImplMixin implements PowerSoundControl {
    @Shadow @Final private LivingEntity owner;
    @Unique private static final CommonConfig.EnhancedConfig ENHANCED_CONFIG = OAPConfig.getInstance().getCommonConfig().enhanced;

    @Unique private static final int REVOKE_COOLDOWN = ENHANCED_CONFIG.revokePowerSoundEffectCooldown;
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
        if (ENHANCED_CONFIG.revokePowerSoundEffect && owner instanceof ServerPlayerEntity player && shouldPlay) {
            int currentTick = owner.age;
            if (currentTick - revokeLastTick >= REVOKE_COOLDOWN) {
                revokeLastTick = currentTick;
                SoundPacket.INSTANCE.send(player, new SoundPacket.SoundData(
                        OAPSoundEvent.REVOKE,
                        ENHANCED_CONFIG.revokePowerVolume, 1f
                ));
            }
        }
    }

    @Unique private static final int GRANT_COOLDOWN = ENHANCED_CONFIG.grantPowerSoundEffectCooldown;
    @Unique private int grantLastTick = -GRANT_COOLDOWN; // 确保第一次可以播放

    @Unique private boolean isFromOrigin;

    @Inject(method = "addPower", at = @At("TAIL"))
    public void addPower(PowerType<?> powerType, Identifier source, CallbackInfoReturnable<Boolean> cir) {
        if (ENHANCED_CONFIG.grantPowerSoundEffect && owner instanceof ServerPlayerEntity player && !isFromOrigin) {
            int currentTick = owner.age;
            if (currentTick - grantLastTick >= GRANT_COOLDOWN) {
                grantLastTick = currentTick;
                SoundPacket.INSTANCE.send(player, new SoundPacket.SoundData(
                        OAPSoundEvent.GRANT,
                        ENHANCED_CONFIG.grantPowerVolume, 1f
                ));
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
