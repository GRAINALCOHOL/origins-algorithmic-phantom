package cn.grainalcohol.network;

import cn.grainalcohol.power.CountdownPower;
import net.minecraft.network.PacketByteBuf;

public class CountdownPacket extends AbstractPacket<CountdownPower>{
    public static final CountdownPacket INSTANCE = new CountdownPacket();

    private CountdownPacket() {
        super("countdown");
    }

    @Override
    protected void write(PacketByteBuf buf, CountdownPower data) {
        buf.writeIdentifier(data.getType().getIdentifier());
        buf.writeInt(data.getCurrentTimer());
        buf.writeInt(data.getIntervalTimer());
        buf.writeBoolean(data.isCountingDown());
    }
}
