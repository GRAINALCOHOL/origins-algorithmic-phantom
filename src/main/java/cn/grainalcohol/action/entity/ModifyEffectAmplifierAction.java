package cn.grainalcohol.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

/**
 * 类型ID: oap:modify_effect_amplifier<br>
 * <br>
 * 修改指定状态效果的效果倍率<br>
 * 有更多可选项和优秀的错误处理机制，减少了能力注册失败的问题
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}, 必选): 将要修改的状态效果ID</li>
 *   <li><b>operation</b> ({@code String}, 必选): 将要如何修改效果倍率，接受“add”或“set”，非法参数不会修改效果倍率</li>
 *   <li><b>value</b> ({@code int}, 可选): 将要参与计算的数值，默认为1</li>
 *   <li><b>is_ambient</b> ({@code Identifier}, 必选): 修改状态效果是否来源于信标</li>
 *   <li><b>show_particles</b> ({@code Identifier}, 必选): 修改状态效果是否会产生粒子</li>
 *   <li><b>show_icon</b> ({@code Identifier}, 必选): 修改状态效果是否会显示在GUI上</li>
 * </ul>
 */
public class ModifyEffectAmplifierAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.IDENTIFIER)
            .add("operation", SerializableDataTypes.STRING)
            .add("value", SerializableDataTypes.INT, 1)
            .add("is_ambient", SerializableDataTypes.BOOLEAN, false)
            .add("show_particles", SerializableDataTypes.BOOLEAN, true)
            .add("show_icon", SerializableDataTypes.BOOLEAN, true);

    @Override
    public void accept(SerializableData.Instance data, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }

        Identifier effectId = data.getId("effect");
        String operation = data.getString("operation");
        int value = data.getInt("value");
        boolean isAmbient = data.getBoolean("is_ambient");
        boolean showParticles = data.getBoolean("show_particles");
        boolean showIcon = data.getBoolean("show_icon");

        StatusEffect effect = Registries.STATUS_EFFECT.get(effectId);

        if (effect == null) return; //无效ID

        StatusEffectInstance effectInstance = livingTarget.getStatusEffect(effect);
        if ((effectInstance == null)) return; // 没有该效果

        // 根据操作类型计算新的倍率
        int amplifier = effectInstance.getAmplifier();
        int newAmplifier = switch (operation) {
            case "add" -> Math.max(0, amplifier + value); // 增加指定倍率
            case "set" -> Math.max(0, value); // 直接设置为指定倍率
            default -> amplifier; // 未知操作类型则保持原样
        };

        newAmplifier = Math.min(newAmplifier, 254);

        // 先移除原有的效果实例
        livingTarget.removeStatusEffect(effect);

        livingTarget.addStatusEffect(new StatusEffectInstance(
                effect,
                effectInstance.getDuration(),
                newAmplifier,
                isAmbient,
                showParticles,
                showIcon
        ));
    }
}
