package cn.grainalcohol.action.entity;

import cn.grainalcohol.power.CountdownPower;
import cn.grainalcohol.util.EntityUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class ToggleCountdownAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("power", SerializableDataTypes.IDENTIFIER, null)
            .add("powers", SerializableDataTypes.IDENTIFIERS, null)
            .add("mode", SerializableDataTypes.STRING, "start");

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        Set<Identifier> powerIds = new HashSet<>();
        if (data.isPresent("power")) {
            powerIds.add(data.getId("power"));
        }
        if (data.isPresent("powers")) {
            powerIds.addAll(data.get("powers"));
        }

        // 添加调试输出
        System.out.println("ToggleCountdownAction executing for power IDs: " + powerIds);

        switch (data.getString("mode").toLowerCase()) {
            case "stop":
                stop(livingEntity, powerIds);
                break;
            case "toggle":
                toggle(livingEntity, powerIds);
                break;
            default:
                start(livingEntity, powerIds);
        }
    }

    private static void start(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> ids.contains(p.getType().getIdentifier()))
                .peek(p -> System.out.println("Starting power: " + p.getType().getIdentifier()))
                .forEach(CountdownPower::start);
    }

    private static void stop(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, false).stream()
                .filter(p -> ids.contains(p.getType().getIdentifier()))
                .peek(p -> System.out.println("Stopping power: " + p.getType().getIdentifier()))
                .forEach(CountdownPower::stop);
    }

    private static void toggle(LivingEntity entity, Set<Identifier> ids) {
        EntityUtil.getPowers(entity, CountdownPower.class, true).stream()
                .filter(p -> ids.contains(p.getType().getIdentifier()))
                .peek(p -> System.out.println("Toggling power: " + p.getType().getIdentifier() +
                        ", current active: " + p.isActive()))
                .forEach(p -> {
                    if (p.isActive()) {
                        p.stop();
                    } else {
                        p.start();
                    }
                });
    }
}
