package grainalcohol.oap.network;

import grainalcohol.oap.power.AdvancementProgressPower;
import net.minecraft.network.PacketByteBuf;

public class AdvancementProgressPacket extends AbstractPacket<AdvancementProgressPower>{
    public static final AdvancementProgressPacket INSTANCE = new AdvancementProgressPacket();

    private AdvancementProgressPacket() {
        super("advancement_progress");
    }

    @Override
    protected void write(PacketByteBuf buf, AdvancementProgressPower data) {
        buf.writeIdentifier(data.getType().getIdentifier());
        buf.writeFloat(data.getProgress());
    }
}
