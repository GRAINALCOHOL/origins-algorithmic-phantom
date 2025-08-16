package grainalcohol.oap.mixin;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.api.PityDataHolder;
import grainalcohol.oap.power.ActionOnEffectGainedPower;
import grainalcohol.oap.power.DamageReflectionFlatPower;
import grainalcohol.oap.power.DamageReflectionPercentPower;
import grainalcohol.oap.util.EntityUtil;
import grainalcohol.oap.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements PityDataHolder {
    @Unique private Map<String, Integer> pityData = new HashMap<>();

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writePityDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!pityData.isEmpty()) {
            NbtCompound pityCompound = new NbtCompound();
            pityData.forEach(pityCompound::putInt);
            nbt.put("PityData", pityCompound);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readPityDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PityData")) {
            NbtCompound pityCompound = nbt.getCompound("PityData");
            pityCompound.getKeys().forEach(key -> {
                pityData.put(key, pityCompound.getInt(key));
            });
        }
    }

    @Override
    public int oap$getPityCount(String poolId) {
        return pityData.getOrDefault(poolId, 0);
    }

    @Override
    public void oap$incrementPity(String poolId) {
        pityData.put(poolId, oap$getPityCount(poolId) + 1);
    }

    @Override
    public void oap$resetPity(String poolId) {
        pityData.put(poolId, 0);
    }

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
                        String mode = power.getMode();

                        switch (mode) {
                            case "original" -> reflection = amount;
                            case "add" -> reflection = amount + power.getAmount();
                            case "flat" -> reflection = power.getAmount();
                            default -> {
                                OAPMod.LOGGER.warn("Unknown mode '{}' for {}", mode, DamageReflectionFlatPower.class.getSimpleName());
                                reflection = 0f;
                            }
                        }

                        reflection = MathUtil.nonNegative(reflection + power.getRandomAddition());
                        DamageSource damageSource = entity.getDamageSources().thorns(attacker);
                        attacker.damage(damageSource, reflection);
                    }
                });
    }
}
