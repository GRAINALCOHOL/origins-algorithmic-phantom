package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

/**
 *  隐藏玩家的状态栏信息，就像创造模式那样
 *
 * @see io.github.apace100.apoli.mixin.InGameHudMixin 实际触发逻辑的Mixin类
 */
public class HideStatusBarsPower extends Power {
    //其实我一开始想做隐藏生命值来着，但是有点难注入，之后再补上
    public static final SerializableData DATA = new SerializableData();

    public HideStatusBarsPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
}
