package cn.grainalcohol.util;

import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface EntityRelation {
    boolean test(LivingEntity a, LivingEntity b);
}
