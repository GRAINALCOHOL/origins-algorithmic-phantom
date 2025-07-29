package grainalcohol.oap.keybind;

import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ExtraKeyBinds {
    public static void init(){
        List<String> keys = List.of("ternary","quaternary","quinary","senary","septenary","octonary","nonary","denary");
        for(String key : keys) {
            KeyBinding binding = new KeyBinding("key.oap."+key+"_active", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.origins");
            ApoliClient.registerPowerKeybinding(binding.getTranslationKey(), binding);
            ApoliClient.registerPowerKeybinding(key, binding);
            KeyBindingHelper.registerKeyBinding(binding);
        }
    }
}
