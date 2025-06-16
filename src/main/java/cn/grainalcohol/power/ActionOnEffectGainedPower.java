package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 类型ID: oap:action_on_effect_gained<br>
 * <br>
 * 实体获得状态效果时执行的操作<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>effect和effects字段都为空时获得任何效果都将触发entity_action字段的操作</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}): 将要匹配的状态效果ID</li>
 *   <li><b>effects</b> ({@code Identifier[]}): 将要匹配的多个状态效果ID</li>
 *   <li><b>entity_action</b> ({@code EntityAction}, 必选): 将要执行的操作</li>
 *   <li><b>include_update</b> ({@code boolean}, 可选): 是否将状态效果升级也视为获得，并触发逻辑，默认为true</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * // 获得毒药效果时立即清除并获得再生
 * {
 *   "type": "oap:action_on_effect_gained",
 *   "effect": "minecraft:poison",
 *   "entity_action": [
 *     {
 *       "type": "apoli:clear_effect",
 *       "effect": "minecraft:poison"
 *     },
 *     {
 *       "type": "apoli:apply_effect",
 *       "effect": {
 *         "effect": "minecraft:regeneration",
 *         "duration": 100,
 *         "amplifier": 1
 *       }
 *     }
 *   ]
 * }
 * }</pre>
 *
 * @see cn.grainalcohol.mixin.LivingEntityMixin 实际触发逻辑的Mixin类
 */
public class ActionOnEffectGainedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            .add("effect", SerializableDataTypes.IDENTIFIER, null)
            .add("effects", SerializableDataTypes.IDENTIFIERS, null)
            .add("include_update", SerializableDataTypes.BOOLEAN, true);

    private final Predicate<LivingEntity> CONDITION;
    private final Consumer<LivingEntity> ENTITY_ACTION;
    private final Set<StatusEffect> EFFECTS;
    private final boolean INCLUDE_UPDATE;

    public boolean shouldIncludeUpdate() {
        return INCLUDE_UPDATE;
    }

    public ActionOnEffectGainedPower(PowerType<?> type, LivingEntity entity, Predicate<LivingEntity> condition, Consumer<LivingEntity> entityAction, Identifier effect, List<Identifier> effects, boolean includeUpdate) {
        super(type, entity);
        CONDITION = condition;
        ENTITY_ACTION = entityAction;
        EFFECTS = new HashSet<>();

        if (effect != null) {
            EFFECTS.add(Registries.STATUS_EFFECT.get(effect));
        }
        if(!effects.isEmpty()) {
            effects.forEach(ids -> {
                StatusEffect temp = Registries.STATUS_EFFECT.get(ids);
                EFFECTS.add(temp);
            });
        }

        INCLUDE_UPDATE = includeUpdate;
    }

    public void onEffectGained(StatusEffectInstance effect) {
        // 检查条件是否满足(如果condition为null则直接执行)
        if((CONDITION == null || CONDITION.test(entity))
                && (EFFECTS.isEmpty() || EFFECTS.contains(effect.getEffectType()))) {
            ENTITY_ACTION.accept(entity);
        }
    }
}
