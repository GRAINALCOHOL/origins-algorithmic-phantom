package cn.grainalcohol.network;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.power.AdvancementProgressPower;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementProgressUpdatePacket {
    public static final Identifier ID = OAPMod.id("advancement_progress_update");

    public static void send(ServerPlayerEntity player, AdvancementProgressPower power) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeIdentifier(power.getType().getIdentifier());
        buf.writeFloat(power.getProgress());
        ServerPlayNetworking.send(player, ID, buf);
    }
}
