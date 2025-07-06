package cn.grainalcohol.condition.entity.enhanced;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 类型ID: oap:status_effect<br>
 * <br>
 * 检测实体身上的状态效果<br>
 * 这是对原版（指origins）状态效果条件的增强版本，提供更精细的控制和匹配逻辑
 * 兼容无限时长状态效果
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}): 要检测的单个状态效果ID</li>
 *   <li><b>effects</b> ({@code Identifier[]}): 要检测的多个状态效果ID列表</li>
 *   <li><b>min_amplifier</b> ({@code int}, 可选): 状态效果的最小效果倍率，默认为0</li>
 *   <li><b>max_amplifier</b> ({@code int}, 可选): 状态效果的最大效果倍率，默认为255</li>
 *   <li><b>min_duration</b> ({@code int}, 可选): 状态效果的最小时长(游戏刻)，以tick为单位，默认为20tick</li>
 *   <li><b>max_duration</b> ({@code int}, 可选): 状态效果的最大时长(游戏刻)，以tick为单位，默认为600tick</li>
 *   <li><b>check_all</b> ({@code boolean}, 可选): 是否需要对指定的所有状态效果进行完全匹配，默认为false</li>
 *   <li><b>require_all</b> ({@code boolean}, 可选): 是否需要持有指定的所有状态效果，默认为false</li>
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
            .add("check_all", SerializableDataTypes.BOOLEAN, false)
            .add("require_all", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        // 是否有任何状态效果，啥也没有肯定是false
        if (livingEntity.getStatusEffects().isEmpty()) {
            return false;
        }

        Set<StatusEffect> effects = new HashSet<>();
        Identifier effectId = data.getId("effect");
        List<Identifier> effectIds = data.get("effects");

        if (effectIds != null) {
            effects.add(Registries.STATUS_EFFECT.get(effectId));
        }
        if (effectIds != null && !effectIds.isEmpty()) {
            effectIds.forEach(id -> {
                StatusEffect effect = Registries.STATUS_EFFECT.get(id);
                if (effect != null) {
                    effects.add(effect);
                }
            });
        }

        boolean requireAll = data.getBoolean("require_all");

        Collection<StatusEffectInstance> effectInstances;
        if (effects.isEmpty()) {
            effectInstances = livingEntity.getStatusEffects();
        } else {
            // 检查是否满足require_all条件
            if (requireAll && !effects.stream().allMatch(livingEntity::hasStatusEffect)) {
                return false;
            }

            effectInstances = livingEntity.getStatusEffects().stream()
                    .filter(e -> effects.contains(e.getEffectType()))
                    .toList();
            // 实体未持有指定的状态效果
            if (effectInstances.isEmpty()) {
                return false;
            }
        }

        return statusEffectProcess(data, effectInstances, data.getBoolean("check_all"));
    }

    private static boolean statusEffectProcess(SerializableData.Instance data, Collection<StatusEffectInstance> instances, boolean checkAll) {
        int minAmplifier = data.getInt("min_amplifier");
        int maxAmplifier = data.getInt("max_amplifier");
        int minDuration = data.getInt("min_duration");
        int maxDuration = data.getInt("max_duration");

        return checkAll ?
        instances.stream().allMatch(instance ->
                instance.getAmplifier() >= minAmplifier
                        && instance.getAmplifier() <= maxAmplifier
                        && (instance.isInfinite() || instance.getDuration() >= minDuration)
                        && instance.getDuration() <= maxDuration
        )
        :
        instances.stream().anyMatch(instance ->
                instance.getAmplifier() >= minAmplifier
                        && instance.getAmplifier() <= maxAmplifier
                        && (instance.isInfinite() || instance.getDuration() >= minDuration)
                        && instance.getDuration() <= maxDuration
        );
    }
}
