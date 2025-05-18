package cn.grainalcohol.condition.bientity;

import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

public class IsTeamMemberCondition implements BiFunction<SerializableData.Instance, Pair<Entity, Entity>, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("invert", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        boolean result = EntityUtil.isTeamMember(entities.getLeft(), entities.getRight());
        return data.getBoolean("invert") ? !result : result;
    }
}
