package grainalcohol.oap.mixin;

import grainalcohol.oap.power.ModifyDrinkingSpeedPower;
import grainalcohol.oap.power.ModifyEatingSpeedPower;
import grainalcohol.oap.util.EntityUtil;
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
        if (user instanceof PlayerEntity && stack.isFood()) {
            EntityUtil.getPowers(user, ModifyEatingSpeedPower.class, false)
                    .forEach(power -> cir.setReturnValue((int) power.apply(cir.getReturnValue())));
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void modifyDrinkingSpeed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        LivingEntity user = (LivingEntity) stack.getHolder();
        if (user instanceof PlayerEntity && stack.getItem() instanceof PotionItem) {
            EntityUtil.getPowers(user, ModifyDrinkingSpeedPower.class, false)
                    .forEach(power -> cir.setReturnValue((int) power.apply(cir.getReturnValue())));
        }
    }
}
