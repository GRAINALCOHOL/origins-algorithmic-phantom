package cn.grainalcohol.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.function.BiFunction;

public class NumberOfPlayerCondition implements BiFunction<SerializableData.Instance, Entity, Boolean> {
    public static final SerializableData DATA = new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT, 1)
            .add("invert", SerializableDataTypes.BOOLEAN, false)
            .add("dimension", SerializableDataTypes.STRING, "any")
            ;

    @Override
    public Boolean apply(SerializableData.Instance data, Entity entity) {
        MinecraftServer server = entity.getServer();
        if (server == null) return false;

        String dimensionCheck = data.getString("dimension_check");
        int amount;

        if("same".equals(dimensionCheck)) {
            //same
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> player.getWorld().getRegistryKey().equals(entity.getWorld().getRegistryKey())) //相同维度
                    .count();
        } else if("other".equals(dimensionCheck)) {
            //other
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> !player.getWorld().getRegistryKey().equals(entity.getWorld().getRegistryKey()))
                    .count();
        } else if(!"any".equals(dimensionCheck)) {
            // 指定id
            amount = (int)server.getPlayerManager().getPlayerList().stream()
                    .filter(player -> player.getWorld().getRegistryKey().getValue().toString().equals(dimensionCheck))
                    .count();
        } else {
            // any
            amount = server.getCurrentPlayerCount();
        }

        int compareTo = data.getInt("compare_to");
        Comparison comparison = data.get("comparison");
        boolean result = comparison.compare(amount, compareTo);
        return data.getBoolean("invert") ? !result : result;
    }
}
