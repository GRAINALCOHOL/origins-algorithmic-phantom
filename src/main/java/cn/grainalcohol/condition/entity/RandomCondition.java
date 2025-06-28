package cn.grainalcohol.condition.entity;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;

import java.util.function.BiFunction;

public class RandomCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("chance", SerializableDataTypes.FLOAT, 1f);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        return MathUtil.randomChance(data.getFloat("chance"));
    }
}
