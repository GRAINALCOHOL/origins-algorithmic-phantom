package cn.grainalcohol.condition.entity.enhanced;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 类型ID: oap:status_effect<br>
 * <br>
 * 检测实体身上的状态效果<br>
 * 这是对原版（指origins）状态效果条件的增强版本，提供更精细的控制和匹配逻辑
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}): 要检测的单个状态效果ID</li>
 *   <li><b>effects</b> ({@code Identifier[]}): 要检测的多个状态效果ID列表</li>
 *   <li><b>min_amplifier</b> ({@code int}, 可选): 状态效果的最小效果倍率，默认为0</li>
 *   <li><b>max_amplifier</b> ({@code int}, 可选): 状态效果的最大效果倍率，默认为255</li>
 *   <li><b>min_duration</b> ({@code int}, 可选): 状态效果的最小时长(游戏刻)，以tick为单位，默认为20tick</li>
 *   <li><b>max_duration</b> ({@code int}, 可选): 状态效果的最大时长(游戏刻)，以tick为单位，默认为600tick</li>
 *   <li><b>invert</b> ({@code boolean}, 可选): 是否反转检测结果，默认为false</li>
 *   <li><b>check_all</b> ({@code boolean}, 可选): 设置是否需要对指定的所有状态效果进行完全匹配，默认为false</li>
 * </ul>
 */
public class EnhancedStatusEffectCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.IDENTIFIER, null)
            .add("effects", SerializableDataTypes.IDENTIFIERS, null)
            .add("min_amplifier", SerializableDataTypes.INT, 0)
            .add("max_amplifier", SerializableDataTypes.INT, 255)
            .add("min_duration", SerializableDataTypes.INT, 20)
            .add("max_duration", SerializableDataTypes.INT, 600)
            .add("invert", SerializableDataTypes.BOOLEAN, false)
            .add("check_all", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        Set<StatusEffect> effects = new HashSet<>();

        // 处理单个effect
        if (data.isPresent("effect")) {
            StatusEffect effect = Registries.STATUS_EFFECT.get(data.getId("effect"));
            if (effect != null) {
                effects.add(effect);
            }
        }

        // 处理多个effects
        if (data.isPresent("effects")) {
            List<Identifier> effectIds = data.get("effects");
            effectIds.forEach(id -> {
                StatusEffect effect = Registries.STATUS_EFFECT.get(id);
                if (effect != null) {
                    effects.add(effect);
                }
            });
        }

        boolean checkAll = data.getBoolean("check_all");
        boolean hasEffect;

        if (effects.isEmpty()) {
            // 如果没有指定任何effect，则检查所有效果
            hasEffect = !livingEntity.getActiveStatusEffects().isEmpty(); // 真没指定：（加反转）无为false，有为true
        } else {
            // 根据check_all决定是anyMatch还是allMatch
            hasEffect = checkAll ?
                    effects.stream().allMatch(livingEntity::hasStatusEffect) : // 所有效果都必须存在
                    effects.stream().anyMatch(livingEntity::hasStatusEffect); // 任意一个效果存在即可
        }

        if(!hasEffect){
            return data.getBoolean("invert");
        }

        // 检查效果强度和时间
        StatusEffectInstance instance =
                livingEntity.getActiveStatusEffects().values().stream()
                .filter(e -> effects.isEmpty() // 未指定任何效果：过滤并保留所有获取到的实例
                        || effects.contains(e.getEffectType())) // 否则（已指定）：过滤并保留包含于指定集合中的实例
                .findFirst() // 因为只需要一个满足条件的效果实例，因此保留第一个即可
                .orElse(null); // 玩家没有任何效果 和 玩家没有与指定效果相同的效果
        // 如果没有过滤出任何效果，那么findFirst()将找不到任何值，则返回null

        if (instance != null) {
            int amplifier = instance.getAmplifier();
            int duration = instance.getDuration();

            if (data.isPresent("min_amplifier") && amplifier < data.getInt("min_amplifier")) {
                hasEffect = false;
            }
            if (data.isPresent("max_amplifier") && amplifier > data.getInt("max_amplifier")) {
                hasEffect = false;
            }
            // 无限持续时间可以通过
            if (data.isPresent("min_duration") &&
                    duration < data.getInt("min_duration") && !instance.isInfinite()) {
                hasEffect = false;
            }
            // 无限持续时间无法通过
            if (data.isPresent("max_duration") &&
                    (instance.isInfinite() || duration > data.getInt("max_duration"))) {
                hasEffect = false;
            }
        }

        return data.getBoolean("invert") ? !hasEffect : hasEffect;
    }
}
