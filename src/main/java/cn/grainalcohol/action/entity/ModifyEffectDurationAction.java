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

public class ModifyEffectDurationAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.STRING)
            .add("operation", SerializableDataTypes.STRING)
            .add("value", SerializableDataTypes.INT, 20)
            .add("is_ambient", SerializableDataTypes.BOOLEAN, false)
            .add("show_particles", SerializableDataTypes.BOOLEAN, true)
            .add("show_icon", SerializableDataTypes.BOOLEAN, true);

    @Override
    public void accept(SerializableData.Instance data, Entity target) {
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }

        String effectId = data.getString("effect");
        String operation = data.getString("operation");
        int value = data.getInt("value");// 操作数值，如果操作类型是add或set则以tick为单位
        boolean isAmbient = data.getBoolean("is_ambient");
        boolean showParticles = data.getBoolean("show_particles");
        boolean showIcon = data.getBoolean("show_icon");

        StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(effectId));

        if (effect == null) return; //无效ID

        StatusEffectInstance effectInstance = livingTarget.getStatusEffect(effect);
        if ((effectInstance == null)) return; // 没有该效果

        if(effectInstance.isInfinite()){
            if ("set".equals(operation) && value != Integer.MAX_VALUE) {
                //移除效果并应用新效果
                livingTarget.removeStatusEffect(effect);
                livingTarget.addStatusEffect(new StatusEffectInstance(
                        effect,
                        value,
                        effectInstance.getAmplifier(),
                        isAmbient,
                        showParticles,
                        showIcon
                ));
            }
            // 其他操作类型
            return;
        }

        // 根据操作类型计算新的持续时间
        int duration = effectInstance.getDuration();
        int newDuration = switch (operation) {
            case "add" -> duration + value; // 增加指定ticks数
            case "multiply" -> duration * value; // 乘以指定倍数
            case "multiply_total" -> duration * (1 + value);
            case "set" -> value; // 直接设置为指定值
            default -> duration; // 未知操作类型则保持原样
        };

        // 先移除原有的效果实例
        livingTarget.removeStatusEffect(effect);

        livingTarget.addStatusEffect(new StatusEffectInstance(
                effect,
                newDuration,
                effectInstance.getAmplifier(), // 保持原有效果等级
                isAmbient,
                showParticles,
                showIcon
        ));
    }
}
