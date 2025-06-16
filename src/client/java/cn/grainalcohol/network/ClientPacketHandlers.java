package cn.grainalcohol.network;

import cn.grainalcohol.power.AdvancementProgressPower;
import cn.grainalcohol.power.CountdownPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ClientPacketHandlers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(TimerUpdatePacket.ID,
                (client, handler, buf, responseSender) -> {
                    int timer = buf.readInt();
                    client.execute(() -> {
                        if(client.player != null) {
                            PowerHolderComponent.getPowers(client.player, CountdownPower.class)
                                    .forEach(power -> power.setTimer(timer));
                        }
                    });
                });

        ClientPlayNetworking.registerGlobalReceiver(AdvancementProgressUpdatePacket.ID,
                (client, handler, buf, responseSender) -> {
                    Identifier powerId = buf.readIdentifier();
                    float progress = buf.readFloat();

                    client.execute(() -> {
                        if(client.player != null) {
                            PowerHolderComponent.getPowers(client.player, AdvancementProgressPower.class)
                                    .stream()
                                    .filter(p -> p.getType().getIdentifier().equals(powerId))
                                    .forEach(p -> p.updateProgressFromPacket(progress));
                        }
                    });
                });
    }
}
