package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.BiConsumer;

/**
 * 类型ID: oap:remove_absorption<br>
 * <br>
 * 移除一定量的伤害吸收量（就是那个黄心）
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>amount</b> ({@code float}, 必选): 将要移除的伤害吸收量</li>
 * </ul>
 */
public class RemoveAbsorptionHeartsAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("amount", SerializableDataTypes.FLOAT);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        float amount = data.getFloat("amount");
        float currentAbsorption = livingEntity.getAbsorptionAmount();

        //确保不低于0
        float newAbsorption = Math.max(0, currentAbsorption - amount);
        livingEntity.setAbsorptionAmount(newAbsorption);
    }
}