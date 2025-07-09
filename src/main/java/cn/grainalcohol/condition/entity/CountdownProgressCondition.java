package cn.grainalcohol.condition.entity;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.util.EntityUtil;
import cn.grainalcohol.util.MathUtil;
import cn.grainalcohol.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 类型ID: oap:countdown_progress<br>
 * 用于检查{@code Countdown}能力倒计时进度
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>power</b> ({@code Identifier}): 需要检查的能力id</li>
 *   <li><b>powers</b> ({@code Identifier[]}): 需要检查的多个能力id</li>
 *   <li><b>comparison</b> ({@code Comparison}, 必选): 比较类型，如">=", "==", "<"等</li>
 *   <li><b>compare_to</b> ({@code float}, 必选): 待比较的完成进度，将被自动修正至0~1之间</li>
 *   <li><b>check_all</b> ({@code boolean}, 可选): 是否对待检查能力进行完全匹配，默认为false</li>
 * </ul>
 */
public class CountdownProgressCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT)
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
        Comparison comparison = data.get("comparison");

        float compareTo = MathUtil.clamp(0f, 1f, data.getFloat("compare_to"));

        return checkAll ?
                powers.stream().allMatch(p -> comparison.compare(p.getCompletionRate(), compareTo)) :
                powers.stream().anyMatch(p -> comparison.compare(p.getCompletionRate(), compareTo));
    }
}
