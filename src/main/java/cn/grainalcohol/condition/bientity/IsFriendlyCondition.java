package cn.grainalcohol.condition.bientity;

import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

// 判断两个生物是否会打起来
public class IsFriendlyCondition implements BiFunction<SerializableData.Instance, Pair<Entity, Entity>, Boolean> {
    public static final SerializableData DATA = new SerializableData();

    @Override
    public Boolean apply(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        Entity actor = entities.getLeft();
        Entity target = entities.getRight();

        if (actor instanceof LivingEntity livingActor && target instanceof LivingEntity livingTarget) {
            return EntityUtil.isFriendlyFor(livingActor, livingTarget);
        }

        return false;
    }
}
