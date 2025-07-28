package cn.grainalcohol.command;

import cn.grainalcohol.config.OAPConfig;
import cn.grainalcohol.config.PityConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class ConfirmClearPityDataCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("confirmclearpitydata")
                .requires(source -> source.hasPermissionLevel(3))
                .then(literal("all")
                        .executes(context -> execute(context, true)))
                .then(literal("save")
                        .executes(context -> execute(context, false)))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, boolean clearAll) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || !(player.getWorld() instanceof ServerWorld serverWorld)) return 0;

        String worldName = serverWorld.getServer().getSaveProperties().getLevelName();
        PityConfig pityConfig = OAPConfig.getInstance().getPityConfig();

        if (clearAll) {
            pityConfig.clearAllData();
            player.sendMessage(Text.literal("已清除所有存档的保底计数").formatted(Formatting.BOLD));
        } else {
            pityConfig.removeSaveData(worldName);
            player.sendMessage(Text.literal("已清除当前存档的保底计数").formatted(Formatting.BOLD));
        }
        return 1;
    }
}
