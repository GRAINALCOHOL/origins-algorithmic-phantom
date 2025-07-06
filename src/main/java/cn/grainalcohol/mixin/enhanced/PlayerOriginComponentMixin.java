package cn.grainalcohol.mixin.enhanced;

import cn.grainalcohol.api.IPowerSoundControl;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.component.PlayerOriginComponent;
import io.github.apace100.origins.origin.Origin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerOriginComponent.class)
public class PlayerOriginComponentMixin {
    @Inject(method = "grantPowersFromOrigin", at = @At("HEAD"), remap = false)
    private void onGrant(Origin origin, PowerHolderComponent powerComponent, CallbackInfo ci) {
        if (powerComponent instanceof IPowerSoundControl control) {
            control.oap$setFromOrigin(true);
        }
    }

    @Inject(method = "grantPowersFromOrigin", at = @At("RETURN"), remap = false)
    private void onEnd(Origin origin, PowerHolderComponent powerComponent, CallbackInfo ci) {
        if (powerComponent instanceof IPowerSoundControl control) {
            control.oap$setFromOrigin(false);
        }
    }
}
