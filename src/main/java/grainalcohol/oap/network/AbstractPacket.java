package grainalcohol.oap.network;

import grainalcohol.oap.OAPMod;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class AbstractPacket<T> {
    protected final Identifier id;

    protected AbstractPacket(String path) {
        this.id = OAPMod.id(path + "_packet");
    }

    public Identifier getId() {
        return id;
    }

    protected abstract void write(PacketByteBuf buf, T data);

    public final void send(ServerPlayerEntity player, T data) {
        if (player == null || player.networkHandler == null) {
            OAPMod.LOGGER.warn("Attempted to send packet to null player or player with null networkHandler");
            return;
        }

        try {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            write(buf, data);
            ServerPlayNetworking.send(player, id, buf);
        } catch (Exception e) {
            OAPMod.LOGGER.error("Failed to send packet {}", id, e);
        }

//        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
//        write(buf, data);
//        ServerPlayNetworking.send(player, id, buf);
    }
}
