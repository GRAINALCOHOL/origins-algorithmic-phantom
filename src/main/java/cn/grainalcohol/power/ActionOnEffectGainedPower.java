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

/**
 * 类型ID: oap:action_on_effect_gained<br>
 * <br>
 * 实体获得状态效果时执行的操作<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>effect和effects字段都为空时获得任何效果都将触发entity_action字段的操作</li>
 *     <li>如果存在默认条件字段则需要通过检查才会触发逻辑</li>
 *     <li>check_all字段为true时，获得指定状态效果时需要同时持有指定的所有状态效果才会触发逻辑</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}): 将要匹配的状态效果ID</li>
 *   <li><b>effects</b> ({@code Identifier[]}): 将要匹配的多个状态效果ID</li>
 *   <li><b>entity_action</b> ({@code EntityAction}, 必选): 将要执行的操作</li>
 *   <li><b>include_update</b> ({@code boolean}, 可选): 是否将状态效果升级也视为获得，并触发逻辑，默认为true</li>
 *   <li><b>check_all</b> ({@code boolean}, 可选): 是否要求持有所有指定的状态效果，默认为false</li>
 * </ul>
 *
 * @see cn.grainalcohol.mixin.LivingEntityMixin 实际触发逻辑的Mixin类
 */
public class ActionOnEffectGainedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            .add("effect", SerializableDataTypes.IDENTIFIER, null)
            .add("effects", SerializableDataTypes.IDENTIFIERS, null)
            .add("include_update", SerializableDataTypes.BOOLEAN, true)
            .add("check_all", SerializableDataTypes.BOOLEAN, false)
            ;

    private final Consumer<LivingEntity> entity_action;
    private final Set<StatusEffect> effects;
    private final boolean include_update;
    private final boolean check_all;

    public boolean shouldIncludeUpdate() {
        return include_update;
    }

    public ActionOnEffectGainedPower(PowerType<?> type, LivingEntity entity, Consumer<LivingEntity> entityAction, Identifier effect, List<Identifier> effects, boolean includeUpdate, boolean checkAll) {
        super(type, entity);
        entity_action = entityAction;
        check_all = checkAll;
        this.effects = new HashSet<>();

        if (effect != null) {
            this.effects.add(Registries.STATUS_EFFECT.get(effect));
        }
        if(!effects.isEmpty()) {
            effects.forEach(ids -> {
                StatusEffect temp = Registries.STATUS_EFFECT.get(ids);
                this.effects.add(temp);
            });
        }

        include_update = includeUpdate;
    }

    public void apply(StatusEffectInstance effect) {
        boolean should;

        if (effects.isEmpty()) {
            should = true;
        } else if (!check_all) {
            should = effects.contains(effect.getEffectType());
        } else {
            should = hasAllEffects();
        }

        if (should) {
            entity_action.accept(entity);
        }
    }

    private boolean hasAllEffects() {
        for (StatusEffect requiredEffect : effects) {
            if (!entity.hasStatusEffect(requiredEffect)) {
                return false;
            }
        }
        return true;
    }
}
