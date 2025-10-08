package grainalcohol.oap.condition.bientity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.BiFunction;

@Environment(EnvType.SERVER)
public class DimensionCondition implements BiFunction<SerializableData.Instance, Pair<Entity, Entity>, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("dimension", SerializableDataTypes.STRING, "any");

    @Override
    public Boolean apply(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        Entity actor = entities.getLeft();
        Entity target = entities.getRight();

        RegistryKey<World> actorWorld = actor.getWorld().getRegistryKey();
        RegistryKey<World> targetWorld = target.getWorld().getRegistryKey();

        String dimensionCheck = data.getString("dimension");

        if("same".equals(dimensionCheck)) {
            //same
            return actorWorld.equals(targetWorld);
        } else if("other".equals(dimensionCheck)) {
            //other
            return !actorWorld.equals(targetWorld);
        } else if ("any".equals(dimensionCheck)){
            // any
            return true;
        } else {
            return false;
        }
    }
}
