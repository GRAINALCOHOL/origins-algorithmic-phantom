package cn.grainalcohol.mixin.enhanced;

import cn.grainalcohol.network.SoundPacket;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.apoli.util.Space;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
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
                    ordinal = 10
            ),
            remap = false
    )
    private static ActionFactory<?> playClientSound(ActionFactory<?> original) {
        if (original.getSerializerId().getPath().equals("play_sound")) {
            SerializableData DATA = original.getSerializableData()
                    .add("client", SerializableDataTypes.BOOLEAN, false);

            BiConsumer<SerializableData.Instance, Entity> action = (data, entity) -> {
                if (data.getBoolean("client")) {
                    if (entity instanceof ServerPlayerEntity serverPlayer) {
                        SoundPacket.INSTANCE.send(serverPlayer,
                                new SoundPacket.SoundData(
                                        data.get("sound"),
                                        data.getFloat("volume"),
                                        data.getFloat("pitch")
                                ));
                    }
                }
                else {
                    SoundCategory category;
                    if(entity instanceof PlayerEntity) {
                        category = SoundCategory.PLAYERS;
                    } else
                    if(entity instanceof HostileEntity) {
                        category = SoundCategory.HOSTILE;
                    } else {
                        category = SoundCategory.NEUTRAL;
                    }
                    entity.getWorld().playSound(null, (entity).getX(), (entity).getY(), (entity).getZ(), data.get("sound"),
                            category, data.getFloat("volume"), data.getFloat("pitch"));
                }
            };

            return new ActionFactory<>(original.getSerializerId(), DATA, action);
        }

        return original;
    }

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
