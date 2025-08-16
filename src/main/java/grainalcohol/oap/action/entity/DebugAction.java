package grainalcohol.oap.action.entity;

import grainalcohol.oap.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class DebugAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("log", SerializableDataTypes.STRING, null)
            .add("id", SerializableDataTypes.STRING, "Minecraft")
            .add("entity_info", SerializableDataTypes.BOOLEAN, true)
            .add("as_warning", SerializableDataTypes.BOOLEAN, false)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) {
            return;
        }
        final Logger LOGGER = LoggerFactory.getLogger(data.getString("id"));
        String log = data.getString("log");

        String entityInfo = String.format(
                "Entity: %s(%s), Pos: %s(%s), Powers: %s",
                entity.getName().getString(),
                entity.getUuid(),
                entity.getBlockPos(),
                entity.getWorld().getRegistryKey().getValue(),
                EntityUtil.getAllPowerIds(entity)
        );

        if (data.getBoolean("entity_info")) {
            if (data.getBoolean("as_warning")) {
                LOGGER.warn(log);
                LOGGER.warn(entityInfo);
            } else {
                LOGGER.info(log);
                LOGGER.info(entityInfo);
            }
        }
    }
}
