package cn.grainalcohol.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityUtil {
    public static boolean isTeamMember(Entity entity, Entity target) {
        if (entity.isTeammate(target)) return true;
        Entity superiorEntity = getSuperiorEntity(entity);
        Entity superiorTarget = getSuperiorEntity(target);
        if (superiorEntity == superiorTarget) return true;
        AbstractTeam thisTeam = superiorEntity.getScoreboardTeam();
        AbstractTeam targetTeam = superiorTarget.getScoreboardTeam();
        if (thisTeam == null || targetTeam == null) return false;
        return thisTeam.isEqual(targetTeam);
    }

    @NotNull
    public static Entity getSuperiorEntity(@NotNull Entity entity) {
        if (entity instanceof Tameable tameable)
        {
            Entity owner = tameable.getOwner();
            if (owner != null) return owner;
        }
        if (entity instanceof Ownable ownable)
        {
            Entity owner = ownable.getOwner();
            if (owner != null) return owner;
        }
        return entity;
    }

    @Nullable
    public static EntityAttribute getAttribute(String attributeId) {
        Identifier id = Identifier.tryParse(attributeId);
        if (id == null) return null;
        return Registries.ATTRIBUTE.get(id);
    }

    /**
     * 获取实体持有的指定类型Power
     * @param entity 目标实体
     * @param powerClass Power类型
     * @return 匹配的Power列表
     * @throws IllegalArgumentException 当实体不是LivingEntity时抛出
     */
    public static <T extends Power> List<T> getPowers(Entity entity, Class<T> powerClass, boolean includeInactive) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity must be a LivingEntity");
        }
        return PowerHolderComponent.KEY.get(entity).getPowers(powerClass, includeInactive);
    }

    /**
     * 通过Power ID获取实体持有的Power
     * @param entity 目标实体
     * @param powerId 要查找的Power ID
     * @return 匹配的Power列表
     */
    public static List<Power> getPowers(LivingEntity entity, Identifier powerId, boolean includeInactive) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity must be a LivingEntity");
        }
        return PowerHolderComponent.KEY.get(entity).getPowers().stream()
                .filter(p -> p.getType().getIdentifier().equals(powerId))
                .filter(p -> includeInactive || p.isActive())
                .collect(Collectors.toList());
    }

    /**
     * 获取实体是否持有指定ID的Power
     * @param entity 目标实体
     * @param powerId Power ID
     * @return 是否持有
     */
    public static boolean hasPower(Entity entity, Identifier powerId) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity must be a LivingEntity");
        }
        return PowerHolderComponent.KEY.get(entity).getPowers().stream()
                .anyMatch(p -> p.getType().getIdentifier().equals(powerId));
    }

    /**
     * 获取实体是否持有指定ID的Power
     * @param entity 目标实体
     * @param power 能力类型
     * @param includeInactive 是否包含未活动的能力
     * @return 是否持有
     */
    public static <T extends Power> boolean hasPower(Entity entity, Class<T> power, boolean includeInactive) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity must be a LivingEntity");
        }
        return !getPowers(entity, power, includeInactive).isEmpty();
    }

    /**
     * 获取实体持有的所有Power ID
     * @param entity 目标实体
     * @return Power ID集合
     * @throws IllegalArgumentException 当实体不是LivingEntity时抛出
     */
    public static Set<Identifier> getAllPowerIds(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("Entity must be a LivingEntity");
        }
        return PowerHolderComponent.KEY.get(entity).getPowers().stream()
                .map(p -> p.getType().getIdentifier())
                .collect(Collectors.toSet());
    }
}
