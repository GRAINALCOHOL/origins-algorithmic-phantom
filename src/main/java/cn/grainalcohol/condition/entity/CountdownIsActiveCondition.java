package cn.grainalcohol.condition.entity;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.util.EntityUtil;
import cn.grainalcohol.util.MiscUtil;
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
 * 类型ID: oap:countdown_is_active<br>
 * <br>
 * 检测指定的倒计时能力是否正在活动<br>
 * <br>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>power</b> ({@code Identifier}): 要检测的单个倒计时能力ID</li>
 *   <li><b>powers</b> ({@code Identifier[]}): 要检测的多个倒计时能力ID列表</li>
 *   <li><b>check_all</b> ({@code boolean}, 可选): 设置是否需要对指定的所有倒计时能力进行完全匹配，默认为false</li>
 * </ul>
 */
public class CountdownIsActiveCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
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

        boolean checkAll = data.getBoolean("check_all");

        List<CountdownPower> powers =
                EntityUtil.getPowers(entity, CountdownPower.class, false).stream()
                .filter(p -> powerIds.isEmpty()
                        || MiscUtil.matchesPowerId(p.getType(), powerIds, living))
                .toList();

        if (powers.isEmpty()) {
            return false;
        }

        return checkAll ? powerIds.size() == powers.size() : true;
    }
}
