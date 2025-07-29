package grainalcohol.oap.condition.entity;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class NameCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("name", SerializableDataTypes.STRING, null)
            .add("names", SerializableDataTypes.STRINGS, null)
            .add("mode", SerializableDataTypes.STRING, "auto") // auto, raw, custom, uuid
            .add("use_regex", SerializableDataTypes.BOOLEAN, false)
            ;

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        Set<String> names = new HashSet<>();

        String dataName = data.getString("name");
        List<String> dataNames = data.get("names");

        if (dataName != null) {
            names.add(dataName);
        }
        if (dataNames != null && !dataNames.isEmpty()) {
            names.addAll(dataNames);
        }

        if (names.isEmpty()) {
            return false;
        }

        String mode = data.getString("mode");
        boolean useRegex = data.getBoolean("use_regex");

        // 验证UUID格式
        if ("uuid".equals(mode)) {
            for (String name : names) {
                try {
                    java.util.UUID.fromString(name);
                } catch (IllegalArgumentException e) {
                    OAPMod.LOGGER.error("Invalid UUID format: {}", name);
                    return false;
                }
            }
        }

        return names.stream()
                .anyMatch(name -> EntityUtil.matchName(entity, name, mode, useRegex));
    }
}
