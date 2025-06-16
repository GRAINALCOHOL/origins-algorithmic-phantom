package cn.grainalcohol.action.bientity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiConsumer;

/**
 * 类型ID: oap:damage_by_attribute<br>
 * <br>
 * 对目标实体造成基于操作实体指定属性一定倍率的伤害<br>
 * <b>注意：</b> 在此{@code BientityAction}操作下，actor是造成伤害的实体，target是受到伤害的实体
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>attribute</b> ({@code Identifier}, 必选): 将要参与伤害量计算的指定属性ID</li>
 *   <li><b>operation</b> ({@code String}, 可选): 将要如何修改属性值，接受“add”、“multiply”或“multiply_total”，非法参数不会修改属性，默认为“multiply”</li>
 *   <li><b>value</b> ({@code float}, 可选): 将要参与计算的数值，默认为1</li>
 *   <li><b>damage_type</b> ({@code Identifier}, 必选): 设置该次伤害的伤害类型</li>
 * </ul>
 */
public class DamageByAttributeAction implements BiConsumer<SerializableData.Instance, Pair<Entity, Entity>> {
    public static final SerializableData DATA = new SerializableData()
            .add("attribute", SerializableDataTypes.IDENTIFIER)
            .add("operation", SerializableDataTypes.STRING, "multiply")
            .add("value", SerializableDataTypes.FLOAT, 1.0f)
            .add("damage_type", SerializableDataTypes.IDENTIFIER)
            ;

    @Override
    public void accept(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        Entity source = entities.getLeft();
        Entity target = entities.getRight();

        if (!(source instanceof LivingEntity livingSource)
                || !(target instanceof LivingEntity livingTarget))
            return;

        Identifier attributeId = data.getId("attribute");
        String operation = data.getString("operation");
        float value = data.getFloat("value");
        Identifier damageTypeId = data.getId("damage_type");

        // 获取属性实例
        EntityAttribute attribute = Registries.ATTRIBUTE.get(attributeId);
        if (attribute == null) return;

        // 计算伤害量
        float baseValue = (float)livingSource.getAttributeValue(attribute);
        System.out.println("Base attribute value: " + baseValue);
        float damageAmount = switch (operation) {
            case "add" -> baseValue + value;
            case "multiply" -> baseValue * value;
            case "multiply_total" -> baseValue * (1 + value);
            default -> baseValue;
        };

        RegistryKey<DamageType> damageKey = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeId);
        DamageSource damageSource = livingTarget.getDamageSources().create(damageKey);

        // 造成伤害
        livingTarget.damage(damageSource, damageAmount);
    }
}
