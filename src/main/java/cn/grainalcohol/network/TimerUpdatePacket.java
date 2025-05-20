package cn.grainalcohol.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class TimerUpdatePacket {
    public static final Identifier ID = new Identifier("oap", "timer_update");
    private final int timer;

    public TimerUpdatePacket(int timer) {
        this.timer = timer;
    }

    public static void encode(TimerUpdatePacket packet, PacketByteBuf buf) {
        buf.writeInt(packet.timer);
    }

    public static TimerUpdatePacket decode(PacketByteBuf buf) {
        return new TimerUpdatePacket(buf.readInt());
    }

    public int getTimer() {
        return timer;
    }
}
