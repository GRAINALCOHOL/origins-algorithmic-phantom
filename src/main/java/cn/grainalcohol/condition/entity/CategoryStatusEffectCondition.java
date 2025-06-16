package cn.grainalcohol.condition.entity;

import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;

import java.util.function.BiFunction;

public class CategoryStatusEffectCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    @Override
    public Boolean apply(SerializableData.Instance instance, Entity entity) {
        //WIP
        return null;
    }
}
