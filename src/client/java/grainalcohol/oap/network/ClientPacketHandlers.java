package grainalcohol.oap.network;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.power.AdvancementProgressPower;
import grainalcohol.oap.power.CountdownPower;
import grainalcohol.oap.temp.TempAdvancement;
import grainalcohol.oap.temp.TempRecipe;
import grainalcohol.oap.util.EntityUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientPacketHandlers {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(
                SoundPacket.INSTANCE.getId(),
                (client, handler, buf, responseSender) -> soundHandle(client, buf)
        );
        ClientPlayNetworking.registerGlobalReceiver(
                CountdownPacket.INSTANCE.getId(),
                (client, handler, buf, responseSender) -> countdownHandle(client, buf)
        );
        ClientPlayNetworking.registerGlobalReceiver(
                AdvancementProgressPacket.INSTANCE.getId(),
                (client, handler, buf, responseSender) -> advancementProgressHandle(client, buf)
        );
        ClientPlayNetworking.registerGlobalReceiver(
                ToastPacket.INSTANCE.getId(),
                (client, handler, buf, responseSender) -> toastHandle(client, buf)
        );
    }

    private static void soundHandle(MinecraftClient client, PacketByteBuf buf) {
        Identifier soundId = buf.readIdentifier();
        float volume = buf.readFloat();
        float pitch = buf.readFloat();

        client.execute(() -> {
            SoundInstance sound = new PositionedSoundInstance(soundId, SoundCategory.PLAYERS, volume, pitch, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, true);
            if (client.player != null) {
                client.getSoundManager().play(sound);
            }
        });
    }

    private static void countdownHandle(MinecraftClient client, PacketByteBuf buf) {
        Identifier powerId = buf.readIdentifier();
        int timer = buf.readInt();
        int intervalTimer = buf.readInt();
        boolean isCountingDown = buf.readBoolean();

        client.execute(() -> {
            if(client.player != null) {
                EntityUtil.getPowers(client.player, CountdownPower.class, true)
                        .stream()
                        .filter(p -> p.getType().getIdentifier().equals(powerId))
                        .forEach(power -> power.updateFromClient(timer, intervalTimer, isCountingDown));
            }
        });
    }

    private static void advancementProgressHandle(MinecraftClient client, PacketByteBuf buf) {
        Identifier powerId = buf.readIdentifier();
        float progress = buf.readFloat();

        client.execute(() -> {
            if(client.player != null) {
                EntityUtil.getPowers(client.player, AdvancementProgressPower.class, true)
                        .stream()
                        .filter(p -> p.getType().getIdentifier().equals(powerId))
                        .forEach(p -> p.updateFromClient(progress));
            }
        });
    }

    private static void toastHandle(MinecraftClient client, PacketByteBuf buf) {
        Text title = buf.readText();
        Text description = buf.readText();
        ItemStack icon = buf.readItemStack();
        String toastType = buf.readString();
        String advancementType = buf.readString();
        String recipeType = buf.readString();

        client.execute(() -> {
            if (client.getToastManager() != null) {
                showToast(
                        client, title, description,
                        icon, toastType, advancementType, recipeType
                );
            }
        });
    }

    private static void showToast(
            MinecraftClient client, Text title, Text description,
            ItemStack icon, String toastType,
            String advancementType, String recipeType
    ) {
        switch (toastType) {
            case "advancement":
                Toast advancementToast = TempAdvancement.createTempAdvancementToast(
                        title, description, icon, advancementType);
                client.getToastManager().add(advancementToast);
                break;
            case "recipe":
                if (!icon.isEmpty()) {
                    RecipeToast.show(client.getToastManager(), new TempRecipe(icon, recipeType));
                }
                break;
            // TutorialToast不能自定义图标
            case "system":
                SystemToast systemToast = SystemToast.create(client,
                        SystemToast.Type.NARRATOR_TOGGLE, title, description);
                client.getToastManager().add(systemToast);
                break;
            default:
                OAPMod.LOGGER.warn("Unknown toast type: {}", toastType);
        }
    }
}
