package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ActionOnEffectGainedPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onStatusEffectApplied", at = @At("TAIL"))
    private void onEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        component.getPowers(ActionOnEffectGainedPower.class)
                .forEach(power -> power.onEffectGained(effect));
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("TAIL"))
    private void onEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        component.getPowers(ActionOnEffectGainedPower.class)
                .forEach(power -> {
                    if(power.shouldIncludeUpdate()) {  // 新增条件检查
                        power.onEffectGained(effect);
                    }
                });
    }
}
