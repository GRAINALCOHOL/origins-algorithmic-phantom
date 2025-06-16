package cn.grainalcohol.mixin;

import cn.grainalcohol.network.AdvancementProgressUpdatePacket;
import cn.grainalcohol.power.AdvancementProgressPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At("TAIL"))
    private void onCriterionGranted(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (owner != null) {
            PowerHolderComponent.getPowers(owner, AdvancementProgressPower.class).forEach(power -> {
                if (power.getAdvancementId().equals(advancement.getId())) {
                    AdvancementProgress progress = ((PlayerAdvancementTracker)(Object)this).getProgress(advancement);

                    power.updateProgress(progress);

                    // 同步进度到客户端
                    AdvancementProgressUpdatePacket.send(owner, power);
                }
            });
        }
    }
}
