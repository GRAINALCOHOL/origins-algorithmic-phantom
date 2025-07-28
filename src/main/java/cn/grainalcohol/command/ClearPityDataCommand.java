package cn.grainalcohol.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static net.minecraft.server.command.CommandManager.literal;

public class ClearPityDataCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("clearpitydata")
                .requires(source -> source.hasPermissionLevel(3))
                .then(literal("all")
                        .executes(context -> execute(context, true)))
                .then(literal("save")
                        .executes(context -> execute(context, false)))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, boolean clearAll) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || !(player.getWorld() instanceof ServerWorld)) return 0;

        String type = clearAll ? "所有" : "当前存档";

        // 发送确认消息
        player.sendMessage(Text.literal("你确认清除" + type + "的保底计数吗？").formatted(Formatting.YELLOW).formatted(Formatting.BOLD));

        // 创建确认和取消按钮
        MutableText confirm = Text.literal(">>>确认<<<")
                .styled(style -> style
                        .withColor(Formatting.GREEN).withBold(true)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/confirmclearpitydata " + (clearAll ? "all" : "save")
                        ))
                );

        MutableText cancel = Text.literal(">>>取消<<<")
                .styled(style -> style
                        .withColor(Formatting.RED).withBold(true)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/cancelclearpitydata"
                        ))
                );

        player.sendMessage(confirm.append("      ").append(cancel));
        return 1;
    }
}
