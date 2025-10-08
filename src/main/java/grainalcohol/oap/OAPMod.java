package grainalcohol.oap;

import grainalcohol.oap.init.*;
import grainalcohol.oap.config.OAPConfig;
import grainalcohol.oap.listener.EntityDeathListener;
import grainalcohol.oap.init.OAPCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OAPMod implements ModInitializer {
	public static final String MOD_ID = "oap";
	public static final Text MOD_NAME = Text.translatable("text.oap.name");
	public static final Text PREFIX = Text.empty()
			.append(Text.literal("[")).append(MOD_NAME).append("]: ");

	public static void modNotify(PlayerEntity player, Text msg) {
		player.sendMessage(PREFIX.copy().append(msg));
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		OAPSoundEvent.init();
		EntityDeathListener.init();
		OAPConfig.getInstance();

		OAPPowerType.init();
		OAPEntityActionType.init();
		OAPEntityConditionType.init();
		OAPBientityActionType.init();
		OAPBientityConditionType.init();
		OAPBlockActionType.init();
		OAPBlockConditionType.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			OAPCommand.init(dispatcher);
		});
		ServerPlayConnectionEvents.JOIN.register((h, s, c) -> {
			Path pityDataFile = Paths.get("config/oap/pity.json");
			PlayerEntity player = h.getPlayer();
			if (Files.exists(pityDataFile)) {
				modNotify(player, Text.translatable("message.oap.pitydata_deprecated")
						.formatted(Formatting.RED).formatted(Formatting.BOLD).formatted(Formatting.UNDERLINE)
						.styled(style -> style
										.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.oap.pitydata_deprecated.hover")))
										.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/oap deletepitydata"))
						)
				);
				modNotify(player, Text.translatable("message.oap.pitydata_notice")
						.formatted(Formatting.RED).formatted(Formatting.BOLD)
				);
			}
		});
	}
}