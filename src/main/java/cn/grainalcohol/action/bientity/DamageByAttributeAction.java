package cn.grainalcohol.action.bientity;

import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.BiConsumer;

public class DamageByAttributeAction implements BiConsumer<SerializableData.Instance, Pair<Entity, Entity>> {
    public static final SerializableData DATA = new SerializableData()
            .add("attribute", SerializableDataTypes.STRING)
            .add("operation", SerializableDataTypes.STRING)
            .add("value", SerializableDataTypes.FLOAT, 1.0f)
            .add("damage_type", SerializableDataTypes.IDENTIFIER);

    @Override
    public void accept(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        Entity source = entities.getLeft();
        Entity target = entities.getRight();

        if (!(source instanceof LivingEntity livingSource)
                || !(target instanceof LivingEntity livingTarget))
            return;

        String attributeId = data.getString("attribute");
        String operation = data.getString("operation");
        float value = data.getFloat("value");
        Identifier damageTypeId = data.getId("damage_type");

        // 获取属性实例
        EntityAttribute attribute = EntityUtil.getAttribute(attributeId);
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

        // 创建伤害源
        RegistryKey<DamageType> damageTypeKey = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeId);
        DamageSource damageSource = livingTarget.getDamageSources().create(damageTypeKey);

        // 造成伤害
        livingTarget.damage(damageSource, damageAmount);
    }
}
