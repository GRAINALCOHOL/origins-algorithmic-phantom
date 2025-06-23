package cn.grainalcohol.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.function.BiFunction;

/**
 * 类型ID: oap:number_of_player<br>
 * <br>
 * 检测服务器或指定维度中的玩家数量<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li><b>"any"</b>: 检查服务器中所有维度的玩家总数</li>
 *     <li><b>"same"</b>: 仅检查与能力持有者相同维度中的玩家数量</li>
 *     <li><b>"other"</b>: 仅检查与能力持有者不同维度中的玩家数量</li>
 *     <li><b>"维度ID"</b>: 检查指定维度中的玩家数量</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>comparison</b> ({@code Comparison}, 必需): 比较类型，如">=", "==", "<"等</li>
 *   <li><b>compare_to</b> ({@code int}, 可选): 要比较的玩家数量，默认为1</li>
 *   <li><b>invert</b> ({@code boolean}, 可选): 是否反转比较结果，默认为false</li>
 *   <li><b>dimension</b> ({@code String}, 可选): 维度检查模式，接受“other”、“any”、“same”或指定维度ID，默认为"any"</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * {
 *   "type": "oap:number_of_player",
 *   "comparison": ">=",
 *   "compare_to": 3,
 *   "dimension": "same"
 * }
 * }</pre>
 * 上述配置在与能力持有者相同的维度中有3名或以上玩家时返回true
 */
public class NumberOfPlayerCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT, 1)
            .add("invert", SerializableDataTypes.BOOLEAN, false)
            .add("dimension", SerializableDataTypes.STRING, "any")
            ;

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        MinecraftServer server = entity.getServer();
        if (server == null) return false;

        String dimensionCheck = data.getString("dimension");
        int amount;

        if("same".equals(dimensionCheck)) {
            //same
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> player.getWorld().getRegistryKey().equals(entity.getWorld().getRegistryKey())) //相同维度
                    .count();
        } else if("other".equals(dimensionCheck)) {
            //other
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> !player.getWorld().getRegistryKey().equals(entity.getWorld().getRegistryKey()))
                    .count();
        } else if(!"any".equals(dimensionCheck)) {
            // 指定id
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> player.getWorld().getRegistryKey().getValue().toString().equals(dimensionCheck))
                    .count();
        } else {
            // any
            amount = server.getCurrentPlayerCount();
        }

        int compareTo = data.getInt("compare_to");
        Comparison comparison = data.get("comparison");
        boolean result = comparison.compare(amount, compareTo);
        return data.getBoolean("invert") ? !result : result;
    }
}
