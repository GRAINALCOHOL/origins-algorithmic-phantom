package grainalcohol.oap.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.function.BiFunction;

/**
 * 类型ID: oap:game_day<br>
 * <br>
 * 检测游戏日<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>游戏日从0开始</li>
 *     <li>游戏日的值绑定于服务端，并不是某个玩家加入某服务器的总时长</li>
 *     <li>原版Minecraft中使用{@code /time set ...}指令会导致游戏日重置为0</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>comparison</b> ({@code Comparison}, 必选): 比较类型，如">=", "==", "<"等</li>
 *   <li><b>compare_to</b> ({@code int}, 可选): 要比较的天数值，默认为0</li>
 * </ul>
 */
public class GameDayCondition implements BiFunction<SerializableData.Instance, Entity, Boolean>{
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT, 0);

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        World world = entity.getWorld();
        if(world instanceof ServerWorld){
            long gameTime = world.getTimeOfDay();
            long dayTime = gameTime / 24000L;

            Comparison comparison = data.get("comparison");
            int compareTo = data.getInt("compare_to");
            return comparison.compare((int)dayTime, compareTo);
        }
        return false;
    }
}
