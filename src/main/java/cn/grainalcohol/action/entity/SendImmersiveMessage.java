package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import toni.immersivemessages.api.ImmersiveMessage;

import java.util.function.BiConsumer;

import static cn.grainalcohol.util.MessageUtil.sendToPlayer;

public class SendImmersiveMessage implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("duration", SerializableDataTypes.FLOAT, 10.0F)
            .add("text", SerializableDataTypes.STRING, null)
            .add("color", SerializableDataTypes.INT, 0xFFFFFF)
            .add("size", SerializableDataTypes.FLOAT, 1.0F)
            .add("offset_x",SerializableDataTypes.FLOAT, 0.0F)
            .add("offset_y", SerializableDataTypes.FLOAT, 0.0F);

//    ImmersiveMessage message = ImmersiveMessage.builder(6.0F, "this is a test text")
//            .color(0xFFFFFF)
//            .fadeIn(2.0F)
//            .fadeOut(2.0F)
//            .size(2f)
//            .anchor(TextAnchor.CENTER_CENTER)
////            .align(TextAnchor.BOTTOM_RIGHT)
//            .y(-1f)
//            ;

    ImmersiveMessage message;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayer){
            float duration = data.getFloat("duration");
            String context = data.getString("text");
            message = ImmersiveMessage.builder(duration, context);

            sendToPlayer(message,serverPlayer);
        }
    }
}
