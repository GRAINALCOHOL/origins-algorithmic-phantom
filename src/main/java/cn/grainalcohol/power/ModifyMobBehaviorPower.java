package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public class ModifyMobBehaviorPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("behavior", SerializableDataTypes.STRING);

    private final Predicate<Entity> ENTITY_CONDITION;
    private final EntityBehavior BEHAVIOR;

    public EntityBehavior getBehavior() {
        return this.BEHAVIOR;
    }

    public Predicate<Entity> getEntityCondition() {
        return this.ENTITY_CONDITION;
    }

    public ModifyMobBehaviorPower(PowerType<?> type, LivingEntity entity, Predicate<Entity> entityCondition, EntityBehavior behavior) {
        super(type, entity);
        ENTITY_CONDITION = entityCondition;
        BEHAVIOR = behavior;
    }

    public enum EntityBehavior {
        FRIENDLY,
        PASSIVE
    }
}
