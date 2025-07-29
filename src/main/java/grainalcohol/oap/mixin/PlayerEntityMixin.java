package grainalcohol.oap.mixin;

import grainalcohol.oap.power.PreventExhaustionPower;
import grainalcohol.oap.util.EntityUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void preventExhaustion(float exhaustion, CallbackInfo ci) {
        EntityUtil.getPowers((PlayerEntity)(Object)this, PreventExhaustionPower.class, false)
                .stream().findAny()
                .ifPresent(power -> ci.cancel());
    }
}
