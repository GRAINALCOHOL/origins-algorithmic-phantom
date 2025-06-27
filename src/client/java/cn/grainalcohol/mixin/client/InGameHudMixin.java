package cn.grainalcohol.mixin.client;

import cn.grainalcohol.power.HideStatusBarsPower;
import cn.grainalcohol.util.EntityUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void hideStatusBars(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            EntityUtil.getPowers(client.player, HideStatusBarsPower.class, false)
                    .stream().findAny()
                    .ifPresent(power -> ci.cancel());
        }
    }
}
