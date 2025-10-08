package grainalcohol.oap.action.entity;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.util.MathUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.BiConsumer;

/**
 * 类型ID: oap:modify_absorption<br>
 * <br>
 * 修改实体持有的伤害吸收量（就是那个黄心）
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>mode</b> ({@code String}, 可选): 运算方式，接受“add”、“set”、“scale”或“multiply”，未知参数不会进行修改，默认为“add”</li>
 *   <li><b>amount</b> ({@code float}, 可选): 将要参与运算的数值</li>
 * </ul>
 */
public class ModifyAbsorptionAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("mode", SerializableDataTypes.STRING, "add")
            .add("amount", SerializableDataTypes.FLOAT, 1f);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) return;
        if (!(entity instanceof LivingEntity livingEntity)) return;

        String mode = data.getString("mode");
        float amount = data.getFloat("amount");
        float currentAbsorption = livingEntity.getAbsorptionAmount();

        switch (mode) {
            case "add" -> livingEntity.setAbsorptionAmount(currentAbsorption + amount);
            case "scale" -> livingEntity.setAbsorptionAmount(currentAbsorption * MathUtil.nonNegative(amount));
            case "multiply" -> livingEntity.setAbsorptionAmount(currentAbsorption * (1 + amount));
            case "set" -> livingEntity.setAbsorptionAmount(MathUtil.nonNegative(amount));
            default -> OAPMod.LOGGER.warn("Unknown mode '{}' for {}", mode, getClass().getSimpleName());
        }
    }
}
