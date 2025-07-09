package cn.grainalcohol.action.entity;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;

public class KnockUpAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("velocity", SerializableDataTypes.FLOAT, 0.0f)
            .add("affect_non_living", SerializableDataTypes.BOOLEAN, false)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if(!data.getBoolean("affect_non_living") && !(entity instanceof LivingEntity)) {
            return;
        }

        float velocity = MathUtil.nonNegative(data.getFloat("velocity"));
        Vec3d original = entity.getVelocity();

        if (entity instanceof PlayerEntity player && player.isOnGround()) {
            entity.setOnGround(false);
            entity.setVelocity(original.x, original.y + 0.1, original.z);
        }

        entity.setVelocity(original.x, original.y + velocity, original.z);
        entity.velocityModified = true;
    }
}
