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
 * 修改进食速度<br>
 * <br>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>amount</b> ({@code float}, 可选): 直接乘在进食速度上，将被自动修正至0.1~10之间，默认为1.0</li>
 * </ul>
 *
 * @see cn.grainalcohol.mixin.ItemMixin 实际触发逻辑的Mixin类
 */
public class ModifyEatingSpeedPower extends Power {
    public static final SerializableData DATA = new SerializableData()
            .add("mode", SerializableDataTypes.STRING, "scale")
            .add("amount", SerializableDataTypes.FLOAT, 1f);

    private final String mode;
    private final float amount;

    public ModifyEatingSpeedPower(PowerType<?> type, LivingEntity entity, String mode, float amount) {
        super(type, entity);
        this.mode = mode;
        this.amount = Math.min(10f, amount);
    }

    public float apply(float originalSpeed) {
        return switch (mode) {
            case "scale" -> originalSpeed * Math.max(0.1f, amount);
            case "multiply" -> originalSpeed * Math.max(0.1f, (1 + amount));
            default -> originalSpeed;
        };
    }
}
