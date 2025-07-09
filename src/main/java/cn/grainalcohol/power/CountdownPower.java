package cn.grainalcohol.power;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.network.CountdownPacket;
import cn.grainalcohol.util.MathUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.HudRendered;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/**
 * 类型ID: oap:countdown<br>
 * <br>
 * 功能更多的，类似{@code Cooldown}能力的能力<br>
 * 能力提供了一个倒计时系统，可以自定义结束操作、间隔操作或间隔时间等<br>
 * 可以使用{@code ToggleCountdownAction}切换倒计时活动状态<br>
 * 可以使用{@code CountdownProgressCondition}检查倒计时进度<br>
 * 可以使用{@code CountdownIsActiveCondition}检查倒计时是否在活动<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>如果存在默认条件字段则需要通过检查才会进行倒计时</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>countdown</b> ({@code int}, 必选): 倒计时时长，以tick为单位</li>
 *   <li><b>ending_action</b> ({@code EntityAction}, 必选): 倒计时结束时对能力持有者的操作</li>
 *   <li><b>immediately_start</b> ({@code boolean}, 可选): 获得能力时是否立刻开始倒计时，默认为{@code true}</li>
 *   <li><b>per_time_action</b> ({@code EntityAction}, 可选): 每间隔一段时间对能力持有者的操作</li>
 *   <li><b>action_interval</b> ({@code int}, 可选): 间隔操作的时间周期，以tick为单位，默认为20tick</li>
 *   <li><b>hud_render</b> (可选): Hud render设置，就像{@code Cooldown}那样，渲染方向与一般进度条相反(从右至左)</li>
 * </ul>
 */
public class CountdownPower extends Power implements HudRendered {
    public static final SerializableData DATA = new SerializableData()
            .add("countdown", SerializableDataTypes.INT, 200)
            .add("ending_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("immediately_start", SerializableDataTypes.BOOLEAN, true)
            .add("per_time_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("action_interval", SerializableDataTypes.INT, 20)
            .add("hud_render", ApoliDataTypes.HUD_RENDER, null);

    private final Consumer<LivingEntity> endingAction;
    private final Consumer<LivingEntity> intervalAction;
    private final int intervalTicks;
    private final int maxCountdown;
    private final HudRender hudRender;

    private int currentTimer;
    private int intervalTimer;
    private boolean isCountingDown;

    private static final Identifier TIMER_UPDATE_PACKET = OAPMod.id("timer_update");

    public CountdownPower(PowerType<?> type, LivingEntity entity, int countdown, boolean immediatelyStart,
                          Consumer<LivingEntity> endingAction, Consumer<LivingEntity> intervalAction,
                          int intervalTicks, HudRender hudRender) {
        super(type, entity);
        this.endingAction = endingAction;
        this.intervalAction = intervalAction;
        this.intervalTicks = intervalTicks;
        this.maxCountdown = countdown;
        this.hudRender = hudRender;

        this.currentTimer = countdown;
        this.intervalTimer = intervalTicks;
        this.isCountingDown = immediatelyStart;

        this.setTicking(true);
    }

    @Override
    public void tick() {
        if (!isCountingDown || !isActive()) {
            return;
        }

        // 处理间隔动作
        if (intervalAction != null) {
            intervalTimer--;
            if (intervalTimer <= 0) {
                intervalAction.accept(entity);
                intervalTimer = intervalTicks;
            }
        }

        // 倒计时逻辑
        currentTimer--;
        syncToClient();

        if (currentTimer <= 0) {
            if (endingAction != null) {
                endingAction.accept(entity);
            }
            stop();
        }
    }

    private void syncToClient() {
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            CountdownPacket.INSTANCE.send(serverPlayer, this);
        }
    }

    public void updateFromClient(int timer, int intervalTimer, boolean countingDown) {
        this.currentTimer = timer;
        this.intervalTimer = intervalTimer;
        this.isCountingDown = countingDown;
    }

    @Override
    public NbtElement toTag() {
        NbtCompound compound = new NbtCompound();
        compound.putInt("currentTimer", currentTimer);
        compound.putInt("intervalTimer", intervalTimer);
        compound.putBoolean("isCountingDown", isCountingDown);
        System.out.println("写入currentTimer" + currentTimer);
        return compound;
    }

    @Override
    public void fromTag(NbtElement tag) {
        if(tag instanceof NbtCompound compound) {
            currentTimer = compound.getInt("currentTimer");
            intervalTimer = compound.getInt("intervalTimer");
            isCountingDown = compound.getBoolean("isCountingDown");
            System.out.println("读取到currentTimer：" + compound.getInt("currentTimer"));
        }
    }

    public int getCurrentTimer() {
        return currentTimer;
    }

    public int getIntervalTimer() {
        return intervalTimer;
    }

    public int getMaxCountdown() {
        return maxCountdown;
    }

    public void start() {
        this.isCountingDown = true;
        syncToClient();
    }

    public void stop() {
        this.isCountingDown = false;
        syncToClient();
    }

    public void restart() {
        reset();
        start();
    }

    public void reset() {
        this.currentTimer = maxCountdown;
        this.intervalTimer = intervalTicks;
        syncToClient();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && isCountingDown();
    }

    public boolean isCountingDown() {
        return isCountingDown;
    }

    public boolean isFinished() {
        return getCompletionRate() == 1f;
    }

    public float getCompletionRate() {
        // 完成率
        return 1f - (maxCountdown > 0 ? MathUtil.clamp(0f, 1f, (float) currentTimer / maxCountdown) : 0.0f);
    }

    @Override
    public HudRender getRenderSettings() {
        return hudRender;
    }

    @Override
    public float getFill() {
        // 倒计时为反向完成率（从1至0）
        return 1f - getCompletionRate();
    }

    @Override
    public boolean shouldRender() {
        return hudRender != null && hudRender.shouldRender() && currentTimer > 0;
    }
}
