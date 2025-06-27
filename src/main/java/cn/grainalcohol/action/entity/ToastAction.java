package cn.grainalcohol.action.entity;

import cn.grainalcohol.network.ToastPacket;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class ToastAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("title", SerializableDataTypes.TEXT)
            .add("description", SerializableDataTypes.TEXT)
            .add("icon", SerializableDataTypes.ITEM_STACK)
            .add("toast_type", SerializableDataTypes.STRING, "system")
            .add("advancement_type", SerializableDataTypes.STRING, "task")
            .add("recipe_type", SerializableDataTypes.STRING, "crafting")
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        // 只对玩家实体发送 toast
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        // 只在服务端执行
        if (entity.getWorld().isClient()) {
            return;
        }

        Text title = data.get("title");
        Text description = data.get("description");
        ItemStack icon = data.get("icon");
        String toastType = data.get("toast_type");

        String advancementType = data.getString("advancement_type");
        String recipeType = data.getString("recipe_type");

        // 发送 toast 数据包到客户端
        ToastPacket.send(player, title, description, icon, toastType, advancementType, recipeType);
    }
}
