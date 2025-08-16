package grainalcohol.oap.action.entity;

import grainalcohol.oap.OAPMod;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ToBlockAction implements BiConsumer<SerializableData.Instance, Entity> {
    public static final SerializableData DATA = new SerializableData()
            .add("position", SerializableDataTypes.VECTOR, new Vec3d(0, 0, 0))
            .add("mode", SerializableDataTypes.STRING, "fixed")
            .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
            .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
            ;

    @Override
    public void accept(SerializableData.Instance data, Entity entity) {
        World world = entity.getWorld();
        Vec3d pos = data.get("position");
        BlockPos blockPos = BlockPos.ofFloored(pos);
        String mode = data.getString("mode");
        Predicate<CachedBlockPosition> blockCondition = data.get("block_condition");
        Consumer<Triple<World, BlockPos, Direction>> blockAction = data.get("block_action");
        switch (mode) {
            case "fixed" -> {
                if (blockCondition != null && blockCondition.test(new CachedBlockPosition(world, blockPos, true))) {
                    if (blockAction != null) blockAction.accept(Triple.of(entity.getWorld(), blockPos, Direction.NORTH));
                }
            }
            case "offset" -> {
                BlockPos offestBlockPos = BlockPos.ofFloored(entity.getPos().add(pos));
                if (blockAction != null) blockAction.accept(Triple.of(world, offestBlockPos, Direction.NORTH));
            }
            default -> OAPMod.LOGGER.warn("Unknown mode '{}' for {}", mode, getClass().getSimpleName());
        }
    }
}
