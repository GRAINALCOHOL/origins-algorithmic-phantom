package cn.grainalcohol.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import toni.immersivemessages.api.ImmersiveMessage;

public class MessageUtil {
    public static void sendToPlayer(String text, float duration, PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld){
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            ImmersiveMessage.builder(duration, text)
                    .color(0xFFFFFF)
                    .fadeIn().fadeOut()
                    .sendServer(serverPlayer);
        }
    }
}
