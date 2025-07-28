package cn.grainalcohol.action.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ToOtherPlayerAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("target_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("target_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("self_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
            .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            .add("max_target", SerializableDataTypes.INT, 0)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        MinecraftServer server = entity.getServer();
        if (server == null) return;

        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();

        int maxTarget = data.getInt("max_target");
        int count = 0;

        for (ServerPlayerEntity target : players) {
            if (target == entity) continue; // 跳过自己

            Consumer<Entity> targetAction = data.get("target_action");
            Predicate<Entity> targetCondition = data.get("target_condition");
            if (targetAction != null && targetCondition.test(target)) {
                targetAction.accept(target);
            }

            Consumer<Pair<Entity, Entity>> bientityAction = data.get("bientity_action");
            Predicate<Pair<Entity, Entity>> bientityCondition = data.get("bientity_condition");
            if (bientityAction != null && bientityCondition.test(new Pair<>(entity, target))) {
                bientityAction.accept(new Pair<>(entity, target));
            }

            count++;
            if (maxTarget > 0 && count >= maxTarget) {
                break;
            }
        }

        Consumer<Entity> selfAction = data.get("self_action");
        Predicate<Entity> selfCondition = data.get("self_condition");
        if (selfAction != null && selfCondition.test(entity)) {
            selfAction.accept(entity);
        }
    }
}
