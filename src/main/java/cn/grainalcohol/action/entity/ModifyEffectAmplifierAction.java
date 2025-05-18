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

public class ModifyEffectAmplifierAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.STRING)
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

        String effectId = data.getString("effect");
        String operation = data.getString("operation");
        int value = data.getInt("value");
        boolean isAmbient = data.getBoolean("is_ambient");
        boolean showParticles = data.getBoolean("show_particles");
        boolean showIcon = data.getBoolean("show_icon");

        StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(effectId));

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
