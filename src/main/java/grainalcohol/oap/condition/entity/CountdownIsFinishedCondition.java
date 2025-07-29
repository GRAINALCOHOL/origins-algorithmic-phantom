package grainalcohol.oap.condition.entity;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.power.CountdownPower;
import grainalcohol.oap.util.EntityUtil;
import grainalcohol.oap.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class CountdownIsFinishedCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("check_all", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            OAPMod.LOGGER.warn("Target is non-living");
            return null;
        }

        Set<Identifier> powerIds = new HashSet<>();
        if (data.isPresent("power")) {
            powerIds.add(data.getId("power"));
        }
        if (data.isPresent("powers")) {
            powerIds.addAll(data.get("powers"));
        }

        List<CountdownPower> powers =
                EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                        .filter(p -> powerIds.isEmpty() || MiscUtil.matchesPowerId(p.getType(), powerIds, living))
                        .toList();

        if (powers.isEmpty()) {
            return false;
        }

        boolean checkAll = data.getBoolean("check_all");

        return checkAll ?
                powers.stream().allMatch(CountdownPower::isFinished) :
                powers.stream().anyMatch(CountdownPower::isFinished);
    }
}
