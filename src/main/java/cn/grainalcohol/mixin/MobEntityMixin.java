package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ModifyMobBehaviorPower;
import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void modifyBehavior(LivingEntity target, CallbackInfo ci) {
        if(target != null) {
            for(ModifyMobBehaviorPower power : EntityUtil.getPowers(target, ModifyMobBehaviorPower.class, false)) {
                if(power.shouldApply((MobEntity) (Object) this)) {
                    boolean shouldCancel = switch (power.getBehavior()) {
                        case FRIENDLY -> true;
                        case PASSIVE -> ((MobEntity) (Object) this).hurtTime <= 0;
                    };
                    if(shouldCancel) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}
