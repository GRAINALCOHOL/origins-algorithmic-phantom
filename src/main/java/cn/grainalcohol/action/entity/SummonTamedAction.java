package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.function.BiConsumer;

import static com.mojang.text2speech.Narrator.LOGGER;

public class SummonTamedAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_type", SerializableDataTypes.ENTITY_TYPE);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return;
        EntityType<?> entityType = data.get("entity_type");
        // 检查实体类型是否可以驯服
        if(!TameableEntity.class.isAssignableFrom(entityType.getBaseClass())) {
            LOGGER.warn("SummonTamedAction try to spawn untamable entity: {}", entityType.getTranslationKey());
            return;
        }
        Entity spawn = entityType.spawn(serverWorld, entity.getBlockPos(), SpawnReason.MOB_SUMMONED);
        if (spawn instanceof TameableEntity tameableEntity &&
                entity instanceof PlayerEntity player)
            tameableEntity.setOwner(player);
    }
}
