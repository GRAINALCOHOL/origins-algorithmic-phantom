package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ActionOnSleepCompletePower;
import cn.grainalcohol.power.PreventExhaustionPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void preventExhaustion(float exhaustion, CallbackInfo ci) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        if(component != null && component.getPowers(PreventExhaustionPower.class).size() > 0) {
            ci.cancel();
        }
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("TAIL"))
    private void onWakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(this);
        component.getPowers(ActionOnSleepCompletePower.class)
                .forEach(ActionOnSleepCompletePower::onSleepComplete);
    }
}
