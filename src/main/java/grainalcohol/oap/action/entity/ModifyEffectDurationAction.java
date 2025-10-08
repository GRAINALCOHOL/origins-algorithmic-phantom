package grainalcohol.oap.action.entity;

import grainalcohol.oap.OAPMod;
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
 * 类型ID: oap:modify_effect_duration<br>
 * <br>
 * 修改指定状态效果的时长，兼容无限时长状态效果（将被视为时长极长且只能被“set”修改）<br>
 * 有更多可选项和优秀的错误处理机制，减少了能力注册失败的问题
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>effect</b> ({@code Identifier}, 必选): 将要修改的状态效果ID</li>
 *   <li><b>mode</b> ({@code String}, 可选): 运算方式，接受“add”、“set”、“scale”或“multiply”，未知参数不会修改时长，默认为“add”</li>
 *   <li><b>amount</b> ({@code float}, 可选): 将要参与计算的数值，以tick为单位，默认为0，计算后的时长为负数时修改时长为无限</li>
 *   <li><b>is_ambient</b> ({@code Identifier}, 必选): 修改状态效果是否来源于信标</li>
 *   <li><b>show_particles</b> ({@code Identifier}, 必选): 修改状态效果是否会产生粒子</li>
 *   <li><b>show_icon</b> ({@code Identifier}, 必选): 修改状态效果是否会显示在GUI上</li>
 * </ul>
 */
public class ModifyEffectDurationAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("effect", SerializableDataTypes.IDENTIFIER, null)
            .add("mode", SerializableDataTypes.STRING, "add")
            .add("amount", SerializableDataTypes.FLOAT, 0f)
            .add("is_ambient", SerializableDataTypes.BOOLEAN, false)
            .add("show_particles", SerializableDataTypes.BOOLEAN, true)
            .add("show_icon", SerializableDataTypes.BOOLEAN, true);

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) return;
        if (!(entity instanceof LivingEntity livingEntity)) return;

        Identifier effectId = data.getId("effect");
        String mode = data.getString("mode");
        float amount = data.getFloat("amount"); // 无敌了我居然之前写的getInt
        boolean isAmbient = data.getBoolean("is_ambient");
        boolean showParticles = data.getBoolean("show_particles");
        boolean showIcon = data.getBoolean("show_icon");

        StatusEffect effect = Registries.STATUS_EFFECT.get(effectId);

        if (effect == null) return; //无效ID

        StatusEffectInstance effectInstance = livingEntity.getStatusEffect(effect);
        if ((effectInstance == null)) return; // 没有该效果

        if(effectInstance.isInfinite()){
            if ("set".equals(mode)) {
                livingEntity.removeStatusEffect(effect);
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        effect,
                        (int) amount,
                        effectInstance.getAmplifier(),
                        isAmbient,
                        showParticles,
                        showIcon
                ));
            }
            return;
        }

        int duration = effectInstance.getDuration();
        int newDuration = switch (mode) {
            case "add" -> duration + (int) amount; //tick
            case "scale" -> (int) (duration * amount);
            case "multiply" -> (int) (duration * (1 + amount));
            case "set" -> (int) amount; // tick
            default -> {
                OAPMod.LOGGER.warn("Unknown mode '{}' for {}", mode, getClass().getSimpleName());
                yield duration;
            }
        };

        int checkedDuration;
        if (newDuration < 0) {
            checkedDuration = -1;
        } else {
            checkedDuration = newDuration;
        }

        livingEntity.removeStatusEffect(effect);
        livingEntity.addStatusEffect(new StatusEffectInstance(
                effect,
                checkedDuration,
                effectInstance.getAmplifier(),
                isAmbient,
                showParticles,
                showIcon
        ));
    }
}
