package cn.grainalcohol.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import toni.immersivemessages.api.ImmersiveMessage;

public class MessageUtil {
    public static void sendToPlayer(ImmersiveMessage message, Entity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayer){
            message.sendServer(serverPlayer);
        }
    }
}
