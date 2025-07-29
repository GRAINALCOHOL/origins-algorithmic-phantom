package grainalcohol.oap.condition.bientity;

import grainalcohol.oap.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.function.BiFunction;

/**
 * 类型ID: oap:is_team_member<br>
 * <br>
 * 检测两个实体是否为队友关系<br>
 * <b>注意：</b> 在此{@code BientityCondition}条件下，actor是检测的源实体，target是检测的目标实体
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>invert</b> ({@code boolean}, 可选): 是否反转检测结果，默认为false</li>
 * </ul>
 */
public class IsTeamMemberCondition implements BiFunction<SerializableData.Instance, Pair<Entity, Entity>, Boolean> {
    public static final SerializableData DATA = new SerializableData();

    @Override
    public Boolean apply(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        return EntityUtil.isTeamMember(entities.getLeft(), entities.getRight());
    }
}
