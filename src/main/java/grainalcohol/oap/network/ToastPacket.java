package grainalcohol.oap.network;

import grainalcohol.oap.action.entity.ToastAction;
import net.minecraft.network.PacketByteBuf;

public class ToastPacket extends AbstractPacket<ToastAction.ToastData>{
    public static final ToastPacket INSTANCE = new ToastPacket();

    private ToastPacket() {
        super("toast");
    }

    @Override
    protected void write(PacketByteBuf buf, ToastAction.ToastData data) {
        buf.writeText(data.title());
        buf.writeText(data.description());
        buf.writeItemStack(data.icon());
        buf.writeString(data.toastType());
        buf.writeString(data.advancementType());
        buf.writeString(data.recipeType());
    }
}
