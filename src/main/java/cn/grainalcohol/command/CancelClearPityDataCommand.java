package cn.grainalcohol.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class CancelClearPityDataCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cancelclearpitydata")
                .requires(source -> source.hasPermissionLevel(3))
                .executes(CancelClearPityDataCommand::execute)
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            player.sendMessage(Text.literal("已取消清除操作").formatted(Formatting.BOLD));
        }
        return 1;
    }
}
