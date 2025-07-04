package cn.grainalcohol.mixin;

import cn.grainalcohol.power.PreventExhaustionPower;
import cn.grainalcohol.util.EntityUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow public abstract float getAbsorptionAmount();

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setAbsorptionAmount(F)V", shift = At.Shift.AFTER))
    private void absorptionFixed(DamageSource source, float amount, CallbackInfo ci) {
        if (this.getAbsorptionAmount() <= 0) {
            ((PlayerEntity) (Object) this).removeStatusEffect(StatusEffects.ABSORPTION);
        }
    }
}
