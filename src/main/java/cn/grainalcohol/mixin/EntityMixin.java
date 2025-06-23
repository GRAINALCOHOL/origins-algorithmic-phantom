package cn.grainalcohol.mixin;

import cn.grainalcohol.power.PreventMovementAxisPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyVariable(method = "move", at = @At("STORE"), ordinal = 1)
    private Vec3d modifyMovement(Vec3d movement) {
        if ((Entity)(Object)this instanceof LivingEntity livingEntity) {
            Vec3d filteredMovement = movement;

            // 检查骑乘者
            for (Entity passenger : livingEntity.getPassengerList()) {
                if (passenger instanceof LivingEntity livingPassenger) {
                    for (PreventMovementAxisPower power : PowerHolderComponent.getPowers(livingPassenger, PreventMovementAxisPower.class)) {
                        filteredMovement = power.filterMovement(filteredMovement);
                    }
                }
            }

            // 检查能力持有者
            for (PreventMovementAxisPower power : PowerHolderComponent.getPowers(livingEntity, PreventMovementAxisPower.class)) {
                filteredMovement = power.filterMovement(filteredMovement);
            }

            return filteredMovement;
        }

        return movement;
    }
}
