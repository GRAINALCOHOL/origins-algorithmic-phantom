package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ModifyEatingSpeedPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void modifyEatingSpeed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        LivingEntity user = (LivingEntity) stack.getHolder();
        if(user instanceof PlayerEntity) {
            //能力仅适用于玩家
            PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
            float original = (float) cir.getReturnValue();
            // 遍历玩家所有修改食用速度的能力
            for(ModifyEatingSpeedPower power : component.getPowers(ModifyEatingSpeedPower.class)) {
                // 应用每个能力的效果
                original = power.modifyEatingSpeed(original);
            }
            cir.setReturnValue((int)original);
        }
    }
}
