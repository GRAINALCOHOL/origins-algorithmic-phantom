package cn.grainalcohol.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class GameDayCondition implements BiFunction<SerializableData.Instance, Entity, Boolean>{
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT)
            .add("invert", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        World world = entity.getWorld();
        if(world instanceof ServerWorld){
            long gameTime = world.getTimeOfDay();
            long dayTime = gameTime / 24000L;

            Comparison comparison = data.get("comparison");
            int compareTo = data.getInt("compare_to");
            boolean result = comparison.compare((int)dayTime, compareTo);
            return data.getBoolean("invert") ? !result : result;
        }
        return false;
    }
}
