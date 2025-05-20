package cn.grainalcohol.mixin;

import cn.grainalcohol.power.ModifyEatingSpeedPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void modifyEatingSpeed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        LivingEntity user = (LivingEntity) stack.getHolder();
        if (user instanceof PlayerEntity) {
            boolean isFood = stack.isFood();
            boolean isPotion = stack.getItem() instanceof PotionItem;

            if (isFood || isPotion) {
                PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
                float original = (float) cir.getReturnValue();
                for (ModifyEatingSpeedPower power : component.getPowers(ModifyEatingSpeedPower.class)) {
                    // 食物总是受影响，药水只在affectsPotions为true时受影响
                    if (isFood || (isPotion && power.affectsPotions())) {
                        original = power.modifyEatingSpeed(original);
                    }
                }
                cir.setReturnValue((int) original);
            }
        }
    }
}
