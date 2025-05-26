package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import toni.immersivemessages.ImmersiveFont;
import toni.immersivemessages.api.ImmersiveMessage;

import java.util.function.BiConsumer;

public class SendImmersiveMessage implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("enable", SerializableDataTypes.BOOLEAN, true);

    ImmersiveMessage message = ImmersiveMessage.builder(6.0F, "this is a test text")
            .color(0xFFFFFF)
            .fadeIn(2.0F)
            .fadeOut(2.0F)
            .size(1);

    @Override
    public void accept(SerializableData.Instance instance, Entity entity) {
        if (entity instanceof PlayerEntity player){
            if (player.getWorld() instanceof ServerWorld){
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                message.sendServer(serverPlayer);
            }
        }
    }
}
