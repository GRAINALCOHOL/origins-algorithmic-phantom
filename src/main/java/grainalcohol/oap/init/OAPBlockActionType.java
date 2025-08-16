package grainalcohol.oap.init;

import grainalcohol.oap.OAPMod;
import grainalcohol.oap.action.block.TestAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.BiConsumer;

public class OAPBlockActionType {
    public static void init() {
//        registerBlockAction(OAPMod.id("test"), TestAction.DATA, new TestAction());
    }

    private static void registerBlockAction(Identifier actionId, SerializableData data, BiConsumer<SerializableData.Instance, Triple<World, BlockPos, Direction>> action) {
        Registry.register(
                ApoliRegistries.BLOCK_ACTION, actionId,
                new ActionFactory<>(
                        actionId, data, action
                )
        );
    }
}
