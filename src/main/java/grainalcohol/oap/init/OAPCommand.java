package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.minecraft.server.command.CommandManager.literal;

public class OAPCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("oap")
                .then(literal("deletepitydata")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(OAPCommand::deletePityDataFile)

                )
        );
    }

    private static int deletePityDataFile(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || !(player.getWorld() instanceof ServerWorld)) return 0;

        try {
            Path filePath = Paths.get("config/oap/pity.json");
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                player.sendMessage(Text.translatable("command.oap.deletepitydata.file_deleted")
                        .formatted(Formatting.GREEN));
                return 1;
            } else {
                player.sendMessage(Text.translatable("command.oap.deletepitydata.file_not_found")
                        .formatted(Formatting.RED));
                return 0;
            }
        } catch (IOException e) {
            player.sendMessage(Text.translatable("command.oap.deletepitydata.delete_failed")
                    .formatted(Formatting.RED));
            OAPMod.LOGGER.error("Failed to delete pity.json file", e);
            return 0;
        }
    }
}
