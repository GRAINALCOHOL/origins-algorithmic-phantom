package grainalcohol.oap;

import grainalcohol.oap.keybind.ExtraKeyBinds;
import grainalcohol.oap.network.ClientPacketHandlers;
import net.fabricmc.api.ClientModInitializer;

public class OAPModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ExtraKeyBinds.init();
		ClientPacketHandlers.init();
	}
}