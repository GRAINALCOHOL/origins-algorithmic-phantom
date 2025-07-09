package cn.grainalcohol.network;

import cn.grainalcohol.OAPMod;
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
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf, data);
        ServerPlayNetworking.send(player, id, buf);
    }
}
