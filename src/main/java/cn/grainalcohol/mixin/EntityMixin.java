package cn.grainalcohol.mixin;

import cn.grainalcohol.power.PreventMovementAxisPower;
import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @ModifyVariable(method = "move", at = @At("STORE"), ordinal = 1)
    private Vec3d modifyMovement(Vec3d movement) {
        if ((Entity)(Object)this instanceof LivingEntity livingEntity) {
            Vec3d filteredMovement = movement;

            // 检查骑乘者
            for (Entity passenger : livingEntity.getPassengerList()) {
                if (passenger instanceof LivingEntity livingPassenger) {
                    for (PreventMovementAxisPower power : EntityUtil.getPowers(livingPassenger, PreventMovementAxisPower.class, false)) {
                        filteredMovement = power.filterMovement(filteredMovement);
                    }
                }
            }

            // 检查能力持有者
            for (PreventMovementAxisPower power : EntityUtil.getPowers(livingEntity, PreventMovementAxisPower.class, false)) {
                filteredMovement = power.filterMovement(filteredMovement);
            }

            return filteredMovement;
        }

        return movement;
    }
}
