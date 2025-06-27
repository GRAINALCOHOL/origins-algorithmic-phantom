package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ActionOnEffectGainedPower;
import cn.grainalcohol.power.DamageReflectionFlatPower;
import cn.grainalcohol.power.DamageReflectionPercentPower;
import cn.grainalcohol.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onStatusEffectApplied", at = @At("TAIL"))
    private void onEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        EntityUtil.getPowers((LivingEntity)(Object)this, ActionOnEffectGainedPower.class, false)
                .forEach(power -> power.apply(effect));
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("TAIL"))
    private void onEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        EntityUtil.getPowers((LivingEntity)(Object)this, ActionOnEffectGainedPower.class, false)
                .stream()
                .filter(ActionOnEffectGainedPower::shouldIncludeUpdate)
                .forEach(power -> power.apply(effect));
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void damageReflectionPercent(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object)this;
        if (entity == null) {
            return;
        }
        EntityUtil.getPowers(entity, DamageReflectionPercentPower.class, false)
                .forEach(power -> {
                    if (Math.random() > power.getChance()) {
                        return;
                    }

                    Entity attacker = source.getAttacker();
                    if (attacker instanceof LivingEntity) {
                        if (power.isCheckSource() && source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) {
                            return;
                        }

                        float reflection = (amount * power.getModifier()) + power.getRandomAddition();
                        DamageSource damageSource = entity.getDamageSources().thorns(attacker);
                        attacker.damage(damageSource, reflection);
                    }
                });
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void damageReflectionFlat(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object)this;
        if (entity == null) {
            return;
        }
        EntityUtil.getPowers(entity, DamageReflectionFlatPower.class, true)
                .forEach(power -> {
                    if (Math.random() > power.getChance()) {
                        return;
                    }

                    Entity attacker = source.getAttacker();
                    if (attacker instanceof LivingEntity) {
                        if (power.isCheckSource() && source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) {
                            return;
                        }

                        float reflection = power.getAmount() + power.getRandomAddition();
                        DamageSource damageSource = entity.getDamageSources().thorns(attacker);
                        attacker.damage(damageSource, reflection);
                    }
                });
    }
}
