package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnSleepCompletePower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("entity_action", ApoliDataTypes.ENTITY_ACTION);

    private final Predicate<LivingEntity> CONDITION;
    private final Consumer<LivingEntity> ENTITY_ACTION;

    public ActionOnSleepCompletePower(PowerType<?> type, LivingEntity entity,
                                      Predicate<LivingEntity> condition, Consumer<LivingEntity> entityAction) {
        super(type, entity);
        this.CONDITION = condition;
        this.ENTITY_ACTION = entityAction;
    }

    public void onSleepComplete() {
        if(CONDITION == null || CONDITION.test(entity)) {
            ENTITY_ACTION.accept(entity);
        }
    }
}
