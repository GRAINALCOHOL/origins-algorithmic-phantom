package cn.grainalcohol.action.bientity;

import cn.grainalcohol.util.MathUtil;
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

import static cn.grainalcohol.OAPMod.*;

/**
 * 类型ID: oap:damage_by_attribute<br>
 * <br>
 * 对目标实体造成基于操作实体指定属性一定倍率的伤害<br>
 * <br>
 * <b>注意：</b> 在此{@code BientityAction}操作下，actor是造成伤害的实体，target是受到伤害的实体
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>attribute</b> ({@code Identifier}, 必选): 将要参与伤害量计算的指定属性ID</li>
 *   <li><b>mode</b> ({@code String}, 可选): 将要如何修改属性值，接受“add”、“scale”或“multiply”，非法参数不会修改属性，默认为“multiply”</li>
 *   <li><b>amount</b> ({@code float}, 可选): 将要参与计算的数值，默认为1</li>
 *   <li><b>damage_type</b> ({@code Identifier}, 必选): 设置该次伤害的伤害类型</li>
 *   <li><b>allow_self_damage</b> ({@code Identifier}, 必选): 是否允许伤害自身，默认为true</li>
 * </ul>
 */
public class DamageByAttributeAction implements BiConsumer<SerializableData.Instance, Pair<Entity, Entity>> {
    public static final SerializableData DATA = new SerializableData()
            .add("attribute", SerializableDataTypes.IDENTIFIER, null)
            .add("mode", SerializableDataTypes.STRING, "multiply")
            .add("amount", SerializableDataTypes.FLOAT, 1.0f)
            .add("damage_type", SerializableDataTypes.IDENTIFIER, null)
            .add("allow_self_damage", SerializableDataTypes.BOOLEAN, true)
            ;

    @Override
    public void accept(SerializableData.Instance data, Pair<Entity, Entity> entities) {
        Entity actor = entities.getLeft();
        Entity target = entities.getRight();

        if (!data.getBoolean("allow_self_damage") && actor == target) {
            return;
        }

        if (!(actor instanceof LivingEntity livingActor)
                || !(target instanceof LivingEntity livingTarget))
            return;

        Identifier attributeId = data.getId("attribute");
        String mode = data.getString("mode");
        float amount = data.getFloat("amount");
        Identifier damageTypeId = data.getId("damage_type");

        if (damageTypeId == null) {
            LOGGER.warn("Damage type cannot be empty");
            return;
        }

        // 获取属性实例
        EntityAttribute attribute = Registries.ATTRIBUTE.get(attributeId);
        if (attribute == null) return;

        float baseValue = (float) livingActor.getAttributeValue(attribute);
        float damageAmount = switch (mode) {
            case "add" -> MathUtil.nonNegative(baseValue + amount);
            case "scale" -> baseValue * MathUtil.nonNegative(amount);
            case "multiply" -> baseValue * MathUtil.nonNegative(1 + amount);
            default -> baseValue;
        };

        RegistryKey<DamageType> damageKey = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeId);
        DamageSource damageSource = livingActor.getDamageSources().create(damageKey);

        livingTarget.damage(damageSource, damageAmount);
    }
}
