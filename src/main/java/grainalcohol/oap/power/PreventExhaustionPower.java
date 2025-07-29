package grainalcohol.oap.power;

import grainalcohol.oap.mixin.PlayerEntityMixin;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

/**
 * 类型ID: oap:prevent_exhaustion<br>
 * <br>
 * 阻止能力持有者累计消耗度（基本等效于饱和效果）<br>
 * <br>
 *
 * @see PlayerEntityMixin 实际触发逻辑的Mixin类
 */
public class PreventExhaustionPower extends Power {
    public static final SerializableData DATA = new SerializableData();

    public PreventExhaustionPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
}
