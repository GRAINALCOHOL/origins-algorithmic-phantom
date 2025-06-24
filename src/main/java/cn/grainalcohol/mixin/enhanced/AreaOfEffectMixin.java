package cn.grainalcohol.mixin.enhanced;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.entity.AreaOfEffectAction;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(AreaOfEffectAction.class)
public class AreaOfEffectMixin {
    @Inject(method = "action", at = @At("HEAD"), cancellable = true)
    private static void shuffleAllTarget(SerializableData.Instance data, Entity entity, CallbackInfo ci){
        Consumer<Pair<Entity, Entity>> bientityAction = data.get("bientity_action");
        Predicate<Pair<Entity, Entity>> bientityCondition = data.get("bientity_condition");
        boolean includeTarget = data.get("include_target");
        double radius = data.get("radius");
        double diameter = radius * 2;
        int maxTarget = data.getInt("max_target");

        List<Entity> allTargets = new ArrayList<>();
        for(Entity check : entity.getWorld().getNonSpectatingEntities(Entity.class, Box.of(entity.getLerpedPos(1F), diameter, diameter, diameter))){
            if (check == entity && !includeTarget)
                continue;
            Pair<Entity, Entity> actorTargetPair = new Pair<>(entity, check);
            if ((bientityCondition == null || bientityCondition.test(actorTargetPair)) &&
                    check.squaredDistanceTo(entity) < radius * radius)
                allTargets.add(check);
        }

        Collections.shuffle(allTargets);

        int count = 0;
        for (Entity target : allTargets) {
            bientityAction.accept(new Pair<>(entity, target));
            count++;
            if (maxTarget > 0 && count >= maxTarget)
                break;
        }

        ci.cancel();
    }

@Inject(method = "getFactory", at = @At("RETURN"), cancellable = true, remap = false)
    private static void addMaxTarget(CallbackInfoReturnable<ActionFactory<Entity>> cir){
        cir.setReturnValue(
                new ActionFactory<>(
                        Apoli.identifier("area_of_effect"),
                        new SerializableData()
                                .add("radius", SerializableDataTypes.DOUBLE, 16D)
                                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
                                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                                .add("include_target", SerializableDataTypes.BOOLEAN, false)
                                .add("max_target",SerializableDataTypes.INT,0),
                        AreaOfEffectAction::action
                )
        );
    }
}
