package cn.grainalcohol.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;

/**
 * 类型ID: oap:hide_status_bars<br>
 * <br>
 * 隐藏玩家的状态栏信息，就像创造模式那样<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>此能力仅影响客户端HUD显示，不影响实际的游戏逻辑</li>
 * </ul>
 *
 * <p><b>示例配置:</b></p>
 * <pre>{@code
 * // 结合条件使用，在特定情况下隐藏状态栏
 * {
 *   "type": "oap:hide_status_bars",
 *   "condition": {
 *     "type": "apoli:gamemode",
 *     "gamemode": "adventure"
 *    }
 * }
 * }</pre>
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
