package grainalcohol.oap.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.registry.ModComponents;

import java.util.function.BiConsumer;

import static com.mojang.text2speech.Narrator.LOGGER;

/**
 * 类型ID: oap:modify_origin<br>
 * <br>
 * 修改玩家在指定起源层上的起源<br>
 * 可以触发{@code ActionOnCallBack}能力中的触发器
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>layer</b> ({@code Identifier}, 必选): 将要修改的起源所在的起源层ID</li>
 *   <li><b>origin</b> ({@code Identifier}, 必选): 将要修改的起源ID</li>
 * </ul>
 */
public class ModifyOriginAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("layer", SerializableDataTypes.IDENTIFIER)
            .add("origin", SerializableDataTypes.IDENTIFIER)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;

        Identifier layerId = data.getId("layer");
        Identifier originId = data.getId("origin");

        OriginLayer layer = OriginLayers.getLayer(layerId);
        if (layer == null) {
            LOGGER.warn("Origin layer '{}' not found", layerId);
            return;
        }

        Origin origin = OriginRegistry.get(originId);

        if (origin == null) {
            LOGGER.warn("Origin '{}' not found", originId);
            return;
        }

        setOrigin(player, layer, origin);
    }

    private void setOrigin(ServerPlayerEntity player, OriginLayer layer, Origin origin) {
        if (!layer.getOrigins().contains(origin.getIdentifier())) {
            LOGGER.warn("Origin {} is not in layer {}", origin.getIdentifier(), layer.getIdentifier());
            return;
        }

        OriginComponent component = ModComponents.ORIGIN.get(player);
        // 记录玩家是否曾经拥有过起源(用于回调触发)
        boolean hadOriginBefore = component.hadOriginBefore();

        component.setOrigin(layer, origin);
        component.sync();

        // 触发回调(处理起源变更的相关逻辑)
        OriginComponent.partialOnChosen(player, hadOriginBefore, origin);
    }
}
