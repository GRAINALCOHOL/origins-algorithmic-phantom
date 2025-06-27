package cn.grainalcohol.network;

import cn.grainalcohol.power.AdvancementProgressPower;
import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.temp.TempAdvancement;
import cn.grainalcohol.temp.TempRecipe;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientPacketHandlers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(TimerUpdatePacket.ID,
                (client, handler, buf, responseSender) -> {
                    Identifier powerId = buf.readIdentifier();
                    int timer = buf.readInt();
                    boolean isCountingDown = buf.readBoolean();

                    client.execute(() -> {
                        if(client.player != null) {
                            PowerHolderComponent.getPowers(client.player, CountdownPower.class)
                                    .stream()
                                    .filter(p -> p.getType().getIdentifier().equals(powerId))
                                    .forEach(power -> power.updateFromPacket(timer, isCountingDown));
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

        ClientPlayNetworking.registerGlobalReceiver(ToastPacket.ID,
                (client, handler, buf, responseSender) -> {
                    Text title = buf.readText();
                    Text description = buf.readText();
                    ItemStack icon = buf.readItemStack();
                    String toastType = buf.readString();
                    String advancementType = buf.readString();
                    String recipeType = buf.readString();

                    client.execute(() -> {
                        if (client.getToastManager() != null) {
                            switch (toastType) {
                                case "advancement":
                                    Toast advancementToast = TempAdvancement.createTempAdvancementToast(title, description, icon, advancementType);
                                    client.getToastManager().add(advancementToast);
                                    break;
                                case "recipe":
                                    // 使用RecipeToast
                                    if (!icon.isEmpty()) {
                                        RecipeToast.show(client.getToastManager(), new TempRecipe(icon, recipeType));
                                    }
                                    break;
                                // TutorialToast不能自定义图标
                                case "system":
                                default:
                                    // 使用SystemToast
                                    // 真能使用吗？？？
                                    SystemToast systemToast = SystemToast.create(client,
                                            SystemToast.Type.NARRATOR_TOGGLE, title, description);
                                    client.getToastManager().add(systemToast);
                                    break;
                            }
                        }
                    });
                });
    }
}
