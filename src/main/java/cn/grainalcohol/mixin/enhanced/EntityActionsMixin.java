package cn.grainalcohol.mixin.enhanced;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;

@Mixin(EntityActions.class)
public class EntityActionsMixin {
    // 本意是用来修bug的，但没成功，先放着吧
    @ModifyArg(
            method = "register()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/apace100/apoli/power/factory/action/EntityActions;register(Lio/github/apace100/apoli/power/factory/action/ActionFactory;)V",
                    ordinal = 32
            ),
            remap = false
    )
    private static ActionFactory<?> modifySpecificRegisterCall(ActionFactory<?> original) {
        if (original.getSerializerId().getPath().equals("grant_power")) {
            SerializableData DATA = new SerializableData()
                    .add("power", ApoliDataTypes.POWER_TYPE)
                    .add("source", SerializableDataTypes.IDENTIFIER);

            BiConsumer<SerializableData.Instance, Entity> action = (data, entity) ->
                    PowerHolderComponent.KEY.maybeGet(entity).ifPresent(component -> {
                        PowerType<?> powerType = data.get("power");
                        component.addPower(powerType, data.getId("source"));
//                        System.out.println("给予能力：" + powerType.getIdentifier());
                        component.sync();
//                        PowerHolderComponent.syncPower(entity, powerType);
//                        System.out.println("完成同步：" + entity.getName().getString());
                    });

            return new ActionFactory<>(original.getSerializerId(), DATA, action);
        }

        return original;
    }
}
