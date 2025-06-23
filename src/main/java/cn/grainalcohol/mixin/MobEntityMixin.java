package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ModifyMobBehaviorPower;
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
            PowerHolderComponent component = PowerHolderComponent.KEY.get(target);
            for(ModifyMobBehaviorPower power : component.getPowers(ModifyMobBehaviorPower.class)) {
                if(power.getEntityCondition() == null || power.getEntityCondition().test((Entity)(Object)this)) {
                    boolean shouldCancel = false;
                    switch(power.getBehavior()) {
                        case FRIENDLY:
                            shouldCancel = true;
                            break;
                        case PASSIVE:
                            shouldCancel = ((MobEntity)(Object)this).hurtTime <= 0;
                            break;
                    }
                    if(shouldCancel) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}
