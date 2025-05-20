package cn.grainalcohol.condition.entity;

import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class CountdownProgressCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT)
            .add("invert", SerializableDataTypes.BOOLEAN, false)
            .add("check_all", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        Set<Identifier> powerIds = new HashSet<>();
        if (data.isPresent("power")) {
            powerIds.add(data.getId("power"));
        }
        if (data.isPresent("powers")) {
            powerIds.addAll(data.get("powers"));
        }

        List<CountdownPower> powers =
                EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> powerIds.isEmpty() || powerIds.contains(p.getType().getIdentifier()))
                .toList();

        if (powers.isEmpty()) {
            System.out.println("WARNING: Entity does not have countdown power: " + powerIds);
            return false;
        }

        boolean checkAll = data.getBoolean("check_all");
        Comparison comparison = data.get("comparison");
        float compareTo = data.getFloat("compare_to");
        boolean invert = data.getBoolean("invert");

        boolean result = checkAll ? powers.stream()
                .allMatch(p -> comparison.compare(p.getCompletionRate(), compareTo)) : powers.stream()
                .anyMatch(p -> comparison.compare(p.getCompletionRate(), compareTo));

        return invert ? !result : result;
    }
}
