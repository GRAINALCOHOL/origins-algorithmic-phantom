package cn.grainalcohol.condition.entity;

import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
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
 *   <li><b>invert</b> ({@code boolean}, 可选): 是否反转检测结果，默认为false</li>
 * </ul>
 */
public class CountdownIsActiveCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("check_all", SerializableDataTypes.BOOLEAN, false)
            .add("invert", SerializableDataTypes.BOOLEAN, false);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        Set<Identifier> powerIds = new HashSet<>();
        if (data.isPresent("power")) {
            powerIds.add(data.getId("power"));
        }
        if (data.isPresent("powers")) {
            powerIds.addAll(data.get("powers"));
        }

        boolean checkAll = data.getBoolean("check_all");
        boolean invert = data.getBoolean("invert");

        List<CountdownPower> powers =
                EntityUtil.getPowers(entity, CountdownPower.class, false).stream()
                .filter(p -> powerIds.isEmpty()
                        || powerIds.contains(p.getType().getIdentifier()))
                .toList();

        if (powers.isEmpty()) {
            System.out.println("WARNING: The entity does not have the specified countdown power: " + powerIds);
            return false;
        }

        boolean result = checkAll ?
                powers.stream().allMatch(CountdownPower::isActive) :
                powers.stream().anyMatch(CountdownPower::isActive);

        return invert ? !result : result;
    }
}
