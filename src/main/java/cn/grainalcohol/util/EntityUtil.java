package cn.grainalcohol.util;

import cn.grainalcohol.OAPMod;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
     * 获取实体是否持有指定类的Power
     * @param entity 目标实体
     * @param power 能力类
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

    public static boolean isFriendly(LivingEntity entity, boolean includeNeutral,
                                     boolean includeGolem, boolean playerFriendly) {
        if (entity instanceof PlayerEntity) {
            return playerFriendly;
        }

        if (isPassive(entity, includeGolem)) {
            if (includeNeutral) {
                return true;
            } else {
                return !(entity instanceof Angerable);
            }
        }

        return false;
    }

    public static boolean isPassive(LivingEntity entity, boolean includeGolem) {
        if (entity instanceof ShulkerEntity) return false;
        if (entity instanceof Monster) return false;
        if (entity instanceof HoglinEntity) return false;

        return entity instanceof AllayEntity ||
                (includeGolem && entity instanceof GolemEntity) ||
                entity instanceof WaterCreatureEntity ||
                entity instanceof PassiveEntity;
    }

    public static boolean isFriendlyBetween(LivingEntity actor, LivingEntity target) {
        // 雪傀儡与怪物
        if (checkRelation(
                actor, target,
                (a, b) -> a instanceof SnowGolemEntity && b instanceof Monster)
        ) return false;

        // 宠物与主人
        if (checkRelation(
                actor, target,
                (a,b) -> a instanceof Tameable tameable && tameable.getOwner() == b)
        ) return true;

        // 激怒
        if (checkRelation(actor, target,
                (a, b) -> {
                    if (a instanceof Angerable angerable) {
                        if (angerable.getAttacker() == b) return false;
                        if (angerable.hasAngerTime() && angerable.getAngryAt() != null) {
                            return !angerable.getAngryAt().equals(b.getUuid());
                        }
                    }
                    return true;
                })
        ) return false;

        // 猫与兔子和幼年海龟
        if (checkRelation(actor, target,
                (a, b) -> (a instanceof CatEntity &&
                        (b instanceof RabbitEntity || (b instanceof TurtleEntity turtle && turtle.isBaby()))))
        ) return false;

        // 豹猫与鸡和幼年海龟
        if (checkRelation(actor, target,
                (a, b) -> (a instanceof OcelotEntity &&
                        (b instanceof ChickenEntity || (b instanceof TurtleEntity turtle && turtle.isBaby()))))
        ) return false;

        // 青蛙与史莱姆类
        if (checkRelation(actor, target,
                (a, b) -> a instanceof FrogEntity &&
                        b instanceof SlimeEntity slime &&
                        slime.getSize() == 1)
        ) return false;

        //美西螈与鱼、鱿鱼、发光鱿鱼、蝌蚪、溺尸、守卫者和远古守卫者
        if (checkRelation(actor, target,
                (a, b) -> a instanceof AxolotlEntity &&
                        (b instanceof FishEntity ||
                                b instanceof SquidEntity ||
                                b instanceof DrownedEntity ||
                                b instanceof GuardianEntity))
        ) return false;

        // 狐狸与鸡、兔子、鳕鱼、鲑鱼、热带鱼和陆地上的幼年海龟
        if (checkRelation(actor, target,
                (a, b) -> a instanceof FoxEntity &&
                        (b instanceof ChickenEntity ||
                                b instanceof RabbitEntity ||
                                b instanceof CodEntity ||
                                b instanceof SalmonEntity ||
                                b instanceof TropicalFishEntity ||
                                (b instanceof TurtleEntity turtle && turtle.isBaby() && turtle.isOnGround())))
        ) return false;

        // 羊驼与攻击者；熊猫与攻击者；海豚与非恶魂、守卫者和远古守卫者的攻击者；
        // 因为麻将的屎山
        // 这部分目前做不了

        // 玩家与怪物
        if (checkRelation(
                actor, target,
                (a, b) -> (a instanceof PlayerEntity && b instanceof Monster))
        ) return false;

        OAPMod.LOGGER.debug("Default relationship between {} and {}: friendly",
                actor.getType().getName().getString(), target.getType().getName().getString());
        OAPMod.LOGGER.debug("UUID info: {}, {}", actor.getUuid(), target.getUuid());
        return true;
    }

    public static boolean checkRelation(LivingEntity a, LivingEntity b, EntityRelation relation) {
        return relation.test(a, b) || relation.test(b, a);
    }

    public static DamageSource createDamageSource(LivingEntity attacker, Identifier damageTypeId) {
        RegistryKey<DamageType> damageKey = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeId);
        return attacker.getDamageSources().create(damageKey, attacker);
    }

    public static boolean matchName(Entity entity, String str, String mode, boolean useRegex) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        String rawName = entity.getType().getName().getString();
        String customName = entity.hasCustomName() ? entity.getCustomName().getString() : null;
        String uuid = entity.getUuid().toString();

        // mode: auto, raw, custom, uuid
        return switch (mode) {
            case "raw" -> MiscUtil.matchString(rawName, str, useRegex);
            case "custom" -> customName != null && MiscUtil.matchString(customName, str, useRegex);
            case "uuid" -> uuid.equals(str);
            case "auto" -> uuid.equals(str) ||
                    (customName != null && MiscUtil.matchString(customName, str, useRegex)) ||
                    MiscUtil.matchString(rawName, str, useRegex);
            default -> false;
        };
    }
}
