package grainalcohol.oap.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
        // 之前居然使用Text类，组件里面那些事件似乎都不兼容的，疏忽了
        MutableText textComponent = data.get("text");
        boolean shouldAnonymous = data.getBoolean("anonymous");
        boolean shouldBroadcast = data.getBoolean("broadcast");

        if (entity.getWorld().isClient()) {
            return;
        }

        MutableText message = textComponent.copy()
                .styled(style -> {
                    Text hoverText = Text.translatable("text.oap.say_action.hover").formatted(Formatting.GRAY);
                    Text hoverTextWithBrackets = Text.empty().append(" (").append(hoverText.copy()).append(")").formatted(Formatting.GRAY);
                    HoverEvent original = style.getHoverEvent();
                    if (original == null || original.getAction() != HoverEvent.Action.SHOW_TEXT) {
                        return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
                    } else {
                        return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Text.empty().append(original.getValue(HoverEvent.Action.SHOW_TEXT)).append(hoverTextWithBrackets))
                        );
                    }
                })
                ;
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
