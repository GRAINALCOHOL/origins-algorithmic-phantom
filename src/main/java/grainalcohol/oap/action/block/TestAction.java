package grainalcohol.oap.action.block;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.BiConsumer;

public class TestAction implements BiConsumer<SerializableData.Instance, Triple<World, BlockPos, Direction>> {
    public static final SerializableData DATA = new SerializableData()
            .add("test", SerializableDataTypes.STRING, "");

    @Override
    public void accept(SerializableData.Instance data, Triple<World, BlockPos, Direction> blockInfo) {
        World world = blockInfo.getLeft();

    }
}
