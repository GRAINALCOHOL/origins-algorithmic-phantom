package grainalcohol.oap.action.entity;

import grainalcohol.oap.power.CountdownPower;
import grainalcohol.oap.util.EntityUtil;
import grainalcohol.oap.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * 类型ID: oap:toggle_countdown<br>
 * 用于切换{@code Countdown}能力活动状态的操作
 *
 * <p><b>JSON字段说明:</b></p>
 * <ul>
 *   <li><b>power</b> ({@code Identifier}): 需要操作的能力id</li>
 *   <li><b>powers</b> ({@code Identifier[]}): 需要操作的多个能力id</li>
 *   <li><b>mode</b> ({@code String}, 可选): 将所有目标倒计时能力切换至指定状态，接受“start”、“restart”、“stop”或“toggle”，默认为“start”</li>
 * </ul>
 */
public class ToggleCountdownAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("mode", SerializableDataTypes.STRING, "start")
            .add("allow_toggle_restart", SerializableDataTypes.BOOLEAN, false)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        Set<Identifier> powerIds = new HashSet<>();
        if (data.isPresent("power")) {
            Identifier id = data.getId("power");
            powerIds.add(data.getId("power"));
        }
        if (data.isPresent("powers")) {
            powerIds.addAll(data.get("powers"));
        }

        switch (data.getString("mode").toLowerCase()) {
            case "stop":
                stop(livingEntity, powerIds);
                break;
            case "restart":
                restart(livingEntity, powerIds);
                break;
            case "toggle":
                toggle(livingEntity, powerIds, data.getBoolean("allow_toggle_restart"));
                break;
            default:
                start(livingEntity, powerIds);
        }
    }

    private static void start(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> MiscUtil.matchesPowerId(p.getType(), ids, entity))
                .forEach(CountdownPower::start);
    }

    private static void stop(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> MiscUtil.matchesPowerId(p.getType(), ids, entity))
                .forEach(CountdownPower::stop);
    }

    private static void restart(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> MiscUtil.matchesPowerId(p.getType(), ids, entity))
                .forEach(CountdownPower::restart);
    }

    private static void toggle(LivingEntity entity, Set<Identifier> ids, boolean allowToggleRestart) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> MiscUtil.matchesPowerId(p.getType(), ids, entity))
                .forEach(p -> {
                    if (p.isCountingDown()) {
                        p.stop();
                    } else if (p.isFinished() && allowToggleRestart) {
                        p.restart();
                    } else {
                        p.start();
                    }
                });
    }
}
