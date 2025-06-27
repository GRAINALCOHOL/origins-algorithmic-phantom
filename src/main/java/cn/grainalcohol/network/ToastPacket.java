package cn.grainalcohol.network;

import cn.grainalcohol.OAPMod;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToastPacket {
    public static final Identifier ID = OAPMod.id("toast");

    public static void send(
            ServerPlayerEntity player, Text title,
            Text description, ItemStack icon, String toastType,
            String advancementType, String recipeType
    ) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeText(title);
        buf.writeText(description);
        buf.writeItemStack(icon);
        buf.writeString(toastType);
        buf.writeString(advancementType);
        buf.writeString(recipeType);

        ServerPlayNetworking.send(player, ID, buf);
    }
}
