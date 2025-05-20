package cn.grainalcohol;

import cn.grainalcohol.keybind.ExtraKeyBinds;
import cn.grainalcohol.network.ClientPacketHandlers;
import net.fabricmc.api.ClientModInitializer;

public class OAPModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ExtraKeyBinds.keyRegister();
		ClientPacketHandlers.register();
	}
}