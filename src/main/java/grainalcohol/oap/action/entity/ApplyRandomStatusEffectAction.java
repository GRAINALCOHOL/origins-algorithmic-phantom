package grainalcohol.oap.action.entity;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 类型ID: oap:apply_random_status_effect<br>
 * <br>
 * 从注册表中随机选取指定数量的状态效果并应用
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>amount</b> ({@code int}, 可选): 将要应用的状态效果数量，默认为1</li>
 *   <li><b>max_duration</b> ({@code int}, 可选): 最大持续时间，以tick为单位，默认为600tick</li>
 *   <li><b>min_duration</b> ({@code int}, 可选): 最小持续时间，以tick为单位，默认为20tick</li>
 *   <li><b>max_amplifier</b> ({@code int}, 可选): 最大效果倍率，默认为255</li>
 *   <li><b>min_amplifier</b> ({@code int}, 可选): 最小效果倍率，默认为0</li>
 * </ul>
 */
public class ApplyRandomStatusEffectAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("amount", SerializableDataTypes.INT, 1)
            .add("max_duration", SerializableDataTypes.INT, 600) //tick
            .add("min_duration", SerializableDataTypes.INT, 20) //tick
            .add("max_amplifier", SerializableDataTypes.INT, 255)
            .add("min_amplifier", SerializableDataTypes.INT, 0)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (entity.getWorld().isClient()) return;
        if(!(entity instanceof LivingEntity livingEntity)) return;

        // 获取所有可用的药水效果
        List<StatusEffect> effects = Registries.STATUS_EFFECT.stream()
                .toList();

        if(effects.isEmpty()) return;

        Random random = Random.create();
        int amount = data.getInt("amount");
        int minDuration = data.getInt("min_duration");
        int maxDuration = data.getInt("max_duration");
        int minAmplifier = data.getInt("min_amplifier");
        int maxAmplifier = data.getInt("max_amplifier");

        for(int i = 0; i < amount; i++) {
            // 随机选择效果
            StatusEffect effect = effects.get(random.nextInt(effects.size()));

            // 随机生成持续时间和放大倍数
            int duration = minDuration + random.nextInt(maxDuration - minDuration + 1);
            int amplifier = minAmplifier + random.nextInt(maxAmplifier - minAmplifier + 1);

            // 应用效果
            livingEntity.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
        }
    }
}
