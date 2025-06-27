package cn.grainalcohol.power;

import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;

/**
 * 类型ID: oap:modify_eating_speed<br>
 * <br>
 * 修改进食速度，可选是否包括饮用速度<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>数值越大进食越快，数值越小进食越慢</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>modifier</b> ({@code float}, 可选): 直接乘在进食速度上，将被自动修正至0.1~10之间，默认为1.0</li>
 *   <li><b>affects_potions</b> ({@code boolean}, 可选): 是否同时修改饮用速度，默认为false</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * // 进食速度翻倍，不影响饮用速度
 * {
 *   "type": "oap:modify_eating_speed",
 *   "modifier": 2.0
 * }
 * }</pre>
 *
 * @see cn.grainalcohol.mixin.ItemMixin 实际触发逻辑的Mixin类
 */
public class ModifyEatingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("modifier", SerializableDataTypes.FLOAT, 1.0f);

    private final float Modifier;

    public ModifyEatingSpeedPower(PowerType<?> type, LivingEntity entity, float modifier) {
        super(type, entity);
        this.Modifier = MathUtil.clamp(0.1f, 10f, modifier);
    }

    public float apply(float originalSpeed) {
        return originalSpeed * Modifier;
    }
}
