package cn.grainalcohol.power;

import cn.grainalcohol.mixin.AdvancementProgressAccessor;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.HudRendered;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 类型ID: oap:advancement_progress<br>
 * <br>
 * 将一个进度的准则完成进度通过起源的方式显示出来<br>
 * <br>
 * <b>注意：</b>
 * <ul>
 *     <li>如果存在默认条件字段则需要通过检查才会触发逻辑</li>
 * </ul>
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>advancement</b> ({@code Identifier}, 必选): 需要监测的进度ID</li>
 *   <li><b>hud_render</b> (可选): Hud render选项</li>
 *   <li><b>on_complete</b> ({@code EntityAction}, 可选): 进度完成时对玩家的操作</li>
 * </ul>
 *
 * @see cn.grainalcohol.mixin.PlayerAdvancementTrackerMixin 实际触发逻辑的Mixin类
 */
public class AdvancementProgressPower extends Power implements HudRendered {
    public static final SerializableData DATA = new SerializableData()
            .add("advancement", SerializableDataTypes.IDENTIFIER)
            .add("hud_render", ApoliDataTypes.HUD_RENDER, null)
            .add("on_complete", ApoliDataTypes.ENTITY_ACTION, null);

    private final Identifier advancementId;
    private final HudRender hudRender;
    private final Consumer<LivingEntity> onComplete;
    private float progress = 0f;

    public AdvancementProgressPower(PowerType<?> type, LivingEntity entity, Identifier advancementId, HudRender hudRender, Consumer<LivingEntity> onComplete) {
        super(type, entity);
        this.advancementId = advancementId;
        this.hudRender = hudRender;
        this.onComplete = onComplete;
    }

    public Identifier getAdvancementId() {
        return advancementId;
    }

    public float getProgress() {
        return progress;
    }

    public void updateFromClient(float progress) {
        this.progress = progress;
    }

    public void updateProgress(AdvancementProgress advancementProgress) {
        if (!isActive()) {
            return;
        }
        if (advancementProgress.isDone()) {
            progress = 1f;
            if (onComplete != null) {
                onComplete.accept(entity);
            }
        } else {
            String[][] requirements = ((AdvancementProgressAccessor) (Object) advancementProgress).getRequirements();
            if (requirements != null && requirements.length > 0) {
                // 基于requirements计算进度
                int completedGroups = getCompletedGroups(advancementProgress, requirements);
                progress = (float) completedGroups / requirements.length;
            } else {
                // 原始逻辑
                int obtained = 0;
                for (String criterion : advancementProgress.getObtainedCriteria()) {
                    obtained++;
                }
                int unobtained = 0;
                for (String criterion : advancementProgress.getUnobtainedCriteria()) {
                    unobtained++;
                }
                progress = (obtained + unobtained) > 0 ? (float) obtained / (obtained + unobtained) : 0f;
            }
        }
    }

    private static int getCompletedGroups(AdvancementProgress advancementProgress, String[][] requirements) {
        // 将已获得的准则存入HashSet，O(n)
        Set<String> obtainedSet = new HashSet<>();
        for (String obtained : advancementProgress.getObtainedCriteria()) {
            obtainedSet.add(obtained);
        }

        int completedGroups = 0;
        // 遍历每个需求组，O(m)
        for (String[] group : requirements) {
            // 检查组内是否有任意准则已完成，O(k)
            for (String criterion : group) {
                if (obtainedSet.contains(criterion)) {
                    completedGroups++;
                    break;
                }
            }
        }
        return completedGroups;
    }

    @Override
    public HudRender getRenderSettings() {
        return hudRender;
    }

    @Override
    public float getFill() {
        return progress;
    }

    @Override
    public boolean shouldRender() {
        return hudRender != null && hudRender.shouldRender() && progress < 1f;
    }
}
