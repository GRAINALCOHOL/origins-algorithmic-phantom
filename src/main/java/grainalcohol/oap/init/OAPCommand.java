package grainalcohol.oap.init;

import grainalcohol.oap.config.OAPConfig;
import grainalcohol.oap.config.PityConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class OAPCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("oap")
                .then(literal("clearpitydata")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(literal("all")
                                .executes(context -> clearPityData(context, true))
                                .then(literal("confirm").executes(context -> clearPityDataConfirm(context, true)))
                        )
                        .then(literal("save")
                                .executes(context -> clearPityData(context, false))
                                .then(literal("confirm").executes(context -> clearPityDataConfirm(context, false)))
                        )
                )
        );
    }

    private static int clearPityDataConfirm(CommandContext<ServerCommandSource> context, boolean clearAll) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || !(player.getWorld() instanceof ServerWorld serverWorld)) return 0;

        String worldName = serverWorld.getServer().getSaveProperties().getLevelName();
        PityConfig pityConfig = OAPConfig.getInstance().getPityConfig();

        if (clearAll) {
            pityConfig.clearAllData();
            player.sendMessage(Text.translatable("command.oap.clearpitydata.all.success").formatted(Formatting.BOLD));
        } else {
            pityConfig.removeSaveData(worldName);
            player.sendMessage(Text.translatable("command.oap.clearpitydata.save.success").formatted(Formatting.BOLD));
        }
        return 1;
    }

    private static int clearPityData(CommandContext<ServerCommandSource> context, boolean clearAll) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || !(player.getWorld() instanceof ServerWorld)) return 0;

        String typeKey = clearAll ?
                "command.oap.clearpitydata.type.all" :
                "command.oap.clearpitydata.type.save";

        player.sendMessage(
                // confirm?
                Text.translatable("command.oap.clearpitydata.confirm_message", Text.translatable(typeKey))
                        .formatted(Formatting.YELLOW).formatted(Formatting.BOLD)
                        .append(Text.literal("\n"))
                        .append(Text.literal("\n"))
                        // check file
                        .append(Text.translatable("command.oap.clearpitydata.open_file")
                                .formatted(Formatting.GREEN).formatted(Formatting.BOLD).formatted(Formatting.UNDERLINE)
                                .styled(style -> style
                                        .withClickEvent(new ClickEvent(
                                                ClickEvent.Action.OPEN_FILE,
                                                "config/oap/pity.json"
                                        ))
                                        .withHoverEvent(new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.translatable("command.oap.clearpitydata.file_hover")
                                        ))
                                )
                        )
                        .append(Text.literal("\n"))
                        .append(Text.literal("\n"))
                        // confirm!
                        .append(Text.translatable("command.oap.clearpitydata.confirm_button")
                                .formatted(Formatting.GREEN).formatted(Formatting.BOLD).formatted(Formatting.UNDERLINE)
                                .styled(style -> style
                                        .withClickEvent(new ClickEvent(
                                                ClickEvent.Action.RUN_COMMAND,
                                                "/oap clearpitydata " + (clearAll ? "all" : "save") + " confirm"
                                        ))
                                        .withHoverEvent(new HoverEvent(
                                                HoverEvent.Action.SHOW_TEXT,
                                                Text.translatable("command.oap.clearpitydata.confirm_hover")
                                        ))
                                )
                        )
        );
        return 1;
    }
}
