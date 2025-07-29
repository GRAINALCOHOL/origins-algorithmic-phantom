package grainalcohol.oap.mixin;

import grainalcohol.oap.power.ActionOnEffectGainedPower;
import grainalcohol.oap.power.DamageReflectionFlatPower;
import grainalcohol.oap.power.DamageReflectionPercentPower;
import grainalcohol.oap.util.EntityUtil;
import grainalcohol.oap.util.MathUtil;
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
    @Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
    private void onEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        EntityUtil.getPowers((LivingEntity)(Object)this, ActionOnEffectGainedPower.class, false)
                .forEach(power -> power.apply(effect));
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("HEAD"))
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
                    Entity attacker = source.getAttacker();
                    if (attacker instanceof LivingEntity) {
                        if (power.isCheckSource() && source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) {
                            return;
                        }

                        float reflection = MathUtil.nonNegative((amount * power.getModifier()) + power.getRandomAddition());
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
                    Entity attacker = source.getAttacker();
                    if (attacker instanceof LivingEntity) {
                        if (power.isCheckSource() && source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) {
                            return;
                        }

                        float reflection;

                        switch (power.getMode()) {
                            case "original" -> reflection = amount;
                            case "add" -> reflection = amount + power.getAmount();
                            case "flat" -> reflection = power.getAmount();
                            default -> reflection = 0f;
                        }

                        reflection = MathUtil.nonNegative(reflection + power.getRandomAddition());
                        DamageSource damageSource = entity.getDamageSources().thorns(attacker);
                        attacker.damage(damageSource, reflection);
                    }
                });
    }
}
