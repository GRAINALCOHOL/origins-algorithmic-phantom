package cn.grainalcohol.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import toni.immersivemessages.api.ImmersiveMessage;

public class MessageUtil {
    public static void sendToPlayer(ImmersiveMessage message, Entity entity, boolean autoFix) {
        if (entity instanceof ServerPlayerEntity serverPlayer){
            if (autoFix) {
                message.y(-1f); //自动修正位置
            }
            message.sendServer(serverPlayer);
        }
    }
}
