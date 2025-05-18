package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnEffectGainedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            .add("effect", SerializableDataTypes.STRING, null)
            .add("effects", SerializableDataTypes.STRINGS, null)
            .add("include_update", SerializableDataTypes.BOOLEAN, true);

    private final Predicate<LivingEntity> CONDITION;
    private final Consumer<LivingEntity> ENTITY_ACTION;
    private final Set<StatusEffect> EFFECTS;
    private final boolean INCLUDE_UPDATE;

    public boolean shouldIncludeUpdate() {
        return INCLUDE_UPDATE;
    }

    public ActionOnEffectGainedPower(PowerType<?> type, LivingEntity entity, Predicate<LivingEntity> condition, Consumer<LivingEntity> entityAction, Set<StatusEffect> effects, boolean includeUpdate) {
        super(type, entity);
        CONDITION = condition;
        ENTITY_ACTION = entityAction;
        EFFECTS = effects;
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
