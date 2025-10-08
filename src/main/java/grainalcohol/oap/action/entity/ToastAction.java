package grainalcohol.oap.action.entity;

import grainalcohol.oap.network.ToastPacket;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class ToastAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("title", SerializableDataTypes.TEXT, null)
            .add("description", SerializableDataTypes.TEXT, null)
            .add("icon", SerializableDataTypes.ITEM_STACK, null)
            .add("toast_type", SerializableDataTypes.STRING, "system")
            .add("advancement_type", SerializableDataTypes.STRING, "task")
            .add("recipe_type", SerializableDataTypes.STRING, "crafting")
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        Text title = data.get("title");
        Text description = data.get("description");
        ItemStack icon = data.get("icon");
        String toastType = data.get("toast_type");

        String advancementType = data.getString("advancement_type");
        String recipeType = data.getString("recipe_type");

        // 发送 toast 数据包到客户端
        ToastPacket.INSTANCE.send(player, new ToastData(title, description, icon, toastType, advancementType, recipeType));
    }

    public record ToastData(
            Text title,
            Text description,
            ItemStack icon,
            String toastType,
            String advancementType,
            String recipeType
    ) {}
}
