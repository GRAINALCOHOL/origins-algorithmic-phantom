package cn.grainalcohol.mixin.enhanced;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.apoli.util.Space;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.util.TriConsumer;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;

@Mixin(EntityActions.class)
public class EntityActionsMixin {
    @ModifyArg(
            method = "register()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/apace100/apoli/power/factory/action/EntityActions;register(Lio/github/apace100/apoli/power/factory/action/ActionFactory;)V",
                    ordinal = 15
            ),
            remap = false
    )
    private static ActionFactory<?> addVelocityPreventGroundCheck(ActionFactory<?> original) {
        if (original.getSerializerId().getPath().equals("add_velocity")) {
            SerializableData DATA = original.getSerializableData()
                    .add("no_dampen", SerializableDataTypes.BOOLEAN, false);

            BiConsumer<SerializableData.Instance, Entity> action = (data, entity) -> {
                if (entity instanceof PlayerEntity
                        && (entity.getWorld().isClient ?
                        !data.getBoolean("client") : !data.getBoolean("server")))
                    return;

                if (data.getBoolean("no_dampen") && entity instanceof PlayerEntity player && player.isOnGround()) {
                    entity.setOnGround(false);
                    entity.setVelocity(entity.getVelocity().x, entity.getVelocity().y + 0.1, entity.getVelocity().z);
                }

                Space space = data.get("space");
                Vector3f vec = new Vector3f(data.getFloat("x"), data.getFloat("y"), data.getFloat("z"));
                TriConsumer<Float, Float, Float> method = entity::addVelocity;
                if(data.getBoolean("set")) {
                    method = entity::setVelocity;
                }
                space.toGlobal(vec, entity);
                method.accept(vec.x, vec.y, vec.z);
                entity.velocityModified = true;
            };

            return new ActionFactory<>(original.getSerializerId(), DATA, action);
        }

        return original;
    }
}
