package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.api.TextAnchor;

import java.util.function.BiConsumer;

import static cn.grainalcohol.util.MiscUtil.*;

public class SendImmersiveMessage implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("message_type", SerializableDataTypes.STRING, null)
            .add("duration", SerializableDataTypes.FLOAT, 10.0F)
            .add("text", SerializableDataTypes.STRING, null)
            .add("color", SerializableDataTypes.INT, 0xFFFFFF)
            .add("size", SerializableDataTypes.FLOAT, 1.0F)
            .add("offset_x",SerializableDataTypes.FLOAT, 0.0F)
            .add("offset_y", SerializableDataTypes.FLOAT, 0.0F)
            .add("align", SerializableDataTypes.STRING, "CENTER_CENTER")
            .add("anchor", SerializableDataTypes.STRING, "CENTER_CENTER")
            .add("fade_in", SerializableDataTypes.FLOAT, 1.0F)
            .add("fade_out", SerializableDataTypes.FLOAT, 1.0F)
//            .add("rainbow", SerializableDataTypes.FLOAT, 2.0F)

            .add("subtitle", SerializableDataTypes.STRING, null)
            .add("subtitle_delay", SerializableDataTypes.FLOAT, 0.0F)
            .add("subtitle_offset", SerializableDataTypes.FLOAT, 10.0F)
            .add("subtitle_color", SerializableDataTypes.INT, 0xFFFFFF)
            .add("subtitle_fade_in", SerializableDataTypes.FLOAT, 1.0F)
            .add("subtitle_fade_out", SerializableDataTypes.FLOAT, 1.0F)
            .add("subtitle_align", SerializableDataTypes.STRING, "CENTER_CENTER")
            .add("subtitle_anchor", SerializableDataTypes.STRING, "CENTER_CENTER")
            ;

    ImmersiveMessage message;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayer){
            float duration = data.getFloat("duration");
            String text = data.getString("text");
            String subtitle = data.getString("subtitle");
            float subtitle_delay = data.getFloat("subtitle_delay");
            float subtitle_offset = data.getFloat("subtitle_offset");
            TextAnchor align = tryGetAnchorFromString(data.getString("align"));
            TextAnchor anchor = tryGetAnchorFromString(data.getString("anchor"));
            TextAnchor subtitle_align = tryGetAnchorFromString(data.getString("subtitle_align"));
            TextAnchor subtitle_anchor = tryGetAnchorFromString(data.getString("subtitle_anchor"));

            switch (data.getString("message_type")) {
                case "normal" : message = ImmersiveMessage.builder(duration, text);
                case "toast" : message = ImmersiveMessage.toast(duration, text, subtitle);
                case "popup" : message = ImmersiveMessage.popup(duration, text, subtitle);
                default : message = ImmersiveMessage.builder(duration, text);
            }

            message.color(data.getInt("color"))
                    .size(data.getFloat("size"))
                    .x(data.getFloat("offset_x"))
                    .y(data.getFloat("offset_y"))
                    .align(align)
                    .anchor(anchor)
                    .fadeIn(data.getFloat("fade_in"))
                    .fadeOut(data.getFloat("fade_out"))
            ;

//            if (data.isPresent("rainbow")) {
//                message.rainbow(data.getFloat("rainbow"));
//            }

            if (subtitle != null) {
                message.subtext(subtitle_delay, subtitle, subtitle_offset,
                        (subtext) -> subtext
                                .color(data.getInt("subtitle_color"))
                                .fadeIn(data.getFloat("subtitle_fade_in"))
                                .fadeIn(data.getFloat("subtitle_fade_out"))
                                .align(subtitle_align)
                                .anchor(subtitle_anchor)
                );
            }

            sendToPlayer(message, serverPlayer, true);
        }
    }
}
