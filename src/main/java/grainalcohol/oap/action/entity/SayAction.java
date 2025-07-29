package grainalcohol.oap.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.BiConsumer;

public class SayAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("text", SerializableDataTypes.TEXT, Text.literal("Hello World"))
            .add("anonymous", SerializableDataTypes.BOOLEAN, false)
            .add("broadcast", SerializableDataTypes.BOOLEAN, true)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        Text textComponent = data.get("text");
        boolean shouldAnonymous = data.getBoolean("anonymous");
        boolean shouldBroadcast = data.getBoolean("broadcast");

        if (entity.getWorld().isClient()) {
            return;
        }

        MutableText message = textComponent.copy();
        PlayerManager manager = Objects.requireNonNull(entity.getServer()).getPlayerManager();

        if (shouldBroadcast) {
            if (shouldAnonymous) {
                manager.broadcast(message, false);
            } else {
                MutableText text = Text.literal("<" + entity.getName().getString() + ">").append(message);
                manager.broadcast(text, false);
            }
        }else if (entity instanceof PlayerEntity player){
            if (shouldAnonymous) {
                player.sendMessage(message, false);
            }else {
                MutableText text = Text.literal("<" + player.getName().getString() + ">").append(message);
                player.sendMessage(text, false);
            }
        }
    }
}
