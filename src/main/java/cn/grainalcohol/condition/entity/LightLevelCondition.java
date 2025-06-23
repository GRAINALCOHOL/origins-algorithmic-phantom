package cn.grainalcohol.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.function.BiFunction;

/**
 * 类型ID: oap:light_level<br>
 * <br>
 * 检测实体所在位置的光照等级是否满足指定条件<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li><b>"combined"</b>: 检测总光照等级（方块光照和天空光照的最大值）</li>
 *     <li><b>"block"</b>: 仅检测方块光照等级（火把、岩浆等发出的光）</li>
 *     <li><b>"sky"</b>: 仅检测天空光照等级（阳光和月光）</li>
 * </ul>
 *
 * <b>JSON字段说明:</b>
 * <ul>
 *     <li><b>comparison</b> ({@code Comparison}, 必选): 比较类型，如">=", "==", "<"等</li>
 *     <li><b>compare_to</b> ({@code Integer}, 必选): 要比较的光照等级值 (0-15)</li>
 *     <li><b>light_type</b> ({@code String}, 可选): 光照类型，接受“combined”、“block”或“sky”。默认为“combined”</li>
 *     <li><b>inverted</b> ({@code boolean}, 可选): 是否反转结果。默认为 false</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * {
 *   "type": "oap:light_level",
 *   "comparison": ">=",
 *   "compare_to": 8,
 *   "light_type": "combined"
 * }
 * }
 * </pre>
 * 这个条件会检测实体所在位置的总光照等级是否大于等于8（通常用于判断是否在明亮区域）
 */
public class LightLevelCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT)
            .add("light_type", SerializableDataTypes.STRING, "combined") // "combined", "block", "sky"
            .add("inverted", SerializableDataTypes.BOOLEAN, false)
            ;

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();

        Comparison comparison = data.get("comparison");
        int compareTo = data.getInt("compare_to");
        String lightType = data.getString("light_type");

        int lightLevel = switch (lightType.toLowerCase()) {
            case "block" -> world.getLightLevel(LightType.BLOCK, pos);
            case "sky" -> world.getLightLevel(LightType.SKY, pos);
            default -> world.getLightLevel(pos);
        };

        boolean result = comparison.compare(lightLevel, compareTo);
        boolean inverted = data.getBoolean("inverted");

        return inverted ? !result : result;
    }
}
