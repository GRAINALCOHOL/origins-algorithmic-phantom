package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;

public class ActionOnAbsorptionChangePower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("increase_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("decrease_action", ApoliDataTypes.ENTITY_ACTION, null);

    private final Consumer<LivingEntity> increase_action;
    private final Consumer<LivingEntity> decrease_action;
    private float lastAbsorption = entity.getAbsorptionAmount();

    public ActionOnAbsorptionChangePower(PowerType<?> type, LivingEntity entity, Consumer<LivingEntity> increaseAction, Consumer<LivingEntity> decreaseAction) {
        super(type, entity);
        increase_action = increaseAction;
        decrease_action = decreaseAction;
    }

    @Override
    public void tick() {
        float current = entity.getAbsorptionAmount();

        if (current > lastAbsorption && increase_action != null) {
            increase_action.accept(entity);
        } else if (current < lastAbsorption && decrease_action != null) {
            decrease_action.accept(entity);
        }

        lastAbsorption = current;
    }
}
