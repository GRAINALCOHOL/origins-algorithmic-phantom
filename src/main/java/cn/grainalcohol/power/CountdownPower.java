package cn.grainalcohol.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.HudRendered;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CountdownPower extends Power implements HudRendered {
    public static final SerializableData DATA = new SerializableData()
            .add("countdown", SerializableDataTypes.INT)
            .add("ending_action", ApoliDataTypes.ENTITY_ACTION)
            .add("immediately_start", SerializableDataTypes.BOOLEAN, true)
            .add("per_time_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("action_interval", SerializableDataTypes.INT, 20)
            .add("condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("hud_render", ApoliDataTypes.HUD_RENDER, null);

    private final Consumer<LivingEntity> ENDING_ACTION;
    private final Consumer<LivingEntity> PER_TIME_ACTION;
    private final int ACTION_INTERVAL;
    private final int COUNTDOWN;
    private int timer;
    private boolean isActive;
    private int perTimeTimer;
    private final Predicate<LivingEntity> CONDITION;
    private final HudRender HUD_RENDER;

    private static final Identifier TIMER_UPDATE_PACKET = new Identifier("oap", "timer_update");

    private Integer initialTick = null;

    public CountdownPower(PowerType<?> type, LivingEntity entity, int countdown,boolean immediatelyStart, Consumer<LivingEntity> endingAction,
                          Consumer<LivingEntity> perTimeAction, int actionInterval,
                          Predicate<LivingEntity> condition, HudRender hudRender) {
        super(type, entity);
        this.isActive = immediatelyStart;
        System.out.println("Power created at: " + System.currentTimeMillis());
        System.out.println("Power created with immediatelyStart: " + immediatelyStart);
        ENDING_ACTION = endingAction;
        PER_TIME_ACTION = perTimeAction;
        ACTION_INTERVAL = actionInterval;
        this.perTimeTimer = actionInterval;
        COUNTDOWN = countdown;
        this.timer = countdown; // 使用countdown作为初始timer值
        CONDITION = condition;
        HUD_RENDER = hudRender;

        this.setTicking(true);
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public void tick() {
        if(initialTick == null) {
            initialTick = entity.age % ACTION_INTERVAL;
            System.out.println("Initial tick set to: " + initialTick);
            return;
        }

        if(!isActive) {
            return;
        }
        if(CONDITION != null && !CONDITION.test(entity)) {
            return;
        }

        if(!isActive || (CONDITION != null && !CONDITION.test(entity))) return;

        if(timer <= 0) {
            ENDING_ACTION.accept(entity);
            isActive = false;
            return;
        }

        int previousTimer = timer;
        timer--;

        if(previousTimer != timer && !entity.getWorld().isClient) {
            // 修复：移除不必要的类型转换
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(timer);
            ServerPlayNetworking.send((ServerPlayerEntity)entity, TIMER_UPDATE_PACKET, buf);
        }

        if(PER_TIME_ACTION != null) {
            perTimeTimer--;
            if(perTimeTimer <= 0) {
                PER_TIME_ACTION.accept(entity);
                perTimeTimer = ACTION_INTERVAL; // 重置计时器
            }
        }
    }

    @Override
    public void onGained() {
//        System.out.println("首次获得能力");
        if (isActive) {
            this.start();
        }
    }

    @Override
    public NbtElement toTag() {
        NbtCompound compound = new NbtCompound();
        compound.putBoolean("isActive", isActive);
        compound.putInt("timer", timer);
        return compound;
    }

    @Override
    public void fromTag(NbtElement tag) {
        if(tag instanceof NbtCompound compound) {
            isActive = compound.getBoolean("isActive");
            timer = compound.getInt("timer");
        }
    }

    public void start() {
        this.isActive = true;
        this.timer = COUNTDOWN;
        System.out.println("Timer reset to: " + timer);
    }

    public void stop() {
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public float getCompletionRate() {
        // 返回已完成的比例 (1 - 剩余比例)，因为这是倒计时而不是正计时
        return 1f - MathHelper.clamp((float)timer / COUNTDOWN, 0.0f, 1.0f);
    }

    @Override
    public HudRender getRenderSettings() {
        return HUD_RENDER;
    }

    @Override
    public float getFill() {
        // 返回剩余时间比例用于HUD显示
        return MathHelper.clamp((float)timer / COUNTDOWN, 0.0f, 1.0f);
    }

    @Override
    public boolean shouldRender() {
        return HUD_RENDER != null && HUD_RENDER.shouldRender() && timer > 0;
    }
}
