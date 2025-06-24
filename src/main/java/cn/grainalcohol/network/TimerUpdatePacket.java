package cn.grainalcohol.network;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.power.CountdownPower;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TimerUpdatePacket {
    public static final Identifier ID = OAPMod.id("timer_update");

    public static void send(ServerPlayerEntity player, CountdownPower power) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeIdentifier(power.getType().getIdentifier());
        buf.writeInt(power.getCurrentTimer());
        buf.writeBoolean(power.isCountingDown());

        ServerPlayNetworking.send(player, ID, buf);
    }
}
