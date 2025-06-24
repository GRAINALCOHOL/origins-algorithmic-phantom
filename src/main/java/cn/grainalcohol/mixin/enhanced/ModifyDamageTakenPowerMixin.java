package cn.grainalcohol.mixin.enhanced;

import cn.grainalcohol.config.ModConfig;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModifyDamageTakenPower.class)
public class ModifyDamageTakenPowerMixin {
    @Inject(method = "executeActions", at = @At("HEAD"), cancellable = true)
    private void onExecuteActions(Entity attacker, CallbackInfo ci) {
        if (!ModConfig.getInstance().power.enableDamageTakenPowerFix) {
            return;
        }

        LivingEntity livingEntity = ((PowerEntityAccessor) this).getEntity();

        if (livingEntity.hurtTime > 0) {
            ci.cancel();
        }
    }
}
