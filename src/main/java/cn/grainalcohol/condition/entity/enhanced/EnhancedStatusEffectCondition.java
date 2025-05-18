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

public class EnhancedStatusEffectCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.STRING, null)
            .add("effects", SerializableDataTypes.STRINGS, null)
            .add("min_amplifier", SerializableDataTypes.INT, null)
            .add("max_amplifier", SerializableDataTypes.INT, null)
            .add("min_duration", SerializableDataTypes.INT, null)
            .add("max_duration", SerializableDataTypes.INT, null)
            .add("invert", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        Set<StatusEffect> effects = new HashSet<>();

        // 处理单个effect
        if (data.isPresent("effect")) {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(data.getString("effect")));
            if (effect != null) {
                effects.add(effect);
            }
        }

        // 处理多个effects
        if (data.isPresent("effects")) {
            List<String> effectIds = data.get("effects");
            effectIds.forEach(id -> {
                StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(id));
                if (effect != null) {
                    effects.add(effect);
                }
            });
        }

        // 如果没有指定任何effect，则检查所有效果
        boolean hasEffect = effects.isEmpty() // 未指定任何效果
                ? !livingEntity.getActiveStatusEffects().isEmpty() // 真没指定：（加反转）无为false，有为true
                : effects.stream().anyMatch(livingEntity::hasStatusEffect); //其实指定了：匹配了任意一个则true

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
