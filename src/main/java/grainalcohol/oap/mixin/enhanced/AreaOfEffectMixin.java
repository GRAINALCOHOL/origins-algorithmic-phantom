package grainalcohol.oap.mixin.enhanced;

import grainalcohol.oap.util.AreaShape;
import grainalcohol.oap.util.MathUtil;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.entity.AreaOfEffectAction;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    @Unique
    private static List<Entity> getEntitiesInShape(AreaShape shape, Entity entity, double radius, double height, boolean includeTarget) {
        double diameter = radius * 2;
        Vec3d center = entity.getLerpedPos(1F);
        double searchHeight = (shape.equals(AreaShape.PRISM) || shape.equals(AreaShape.CYLINDER)) ? height : diameter;

        List<Entity> candidates = entity.getWorld()
                .getNonSpectatingEntities(Entity.class, Box.of(center, diameter, searchHeight, diameter))
                .stream()
                .filter(check -> includeTarget || check != entity) // 排除自身(当includeTarget为false时)
                .toList();

        List<Entity> entities = new ArrayList<>();

        switch(shape) {
            case CUBE -> {
                return candidates;
            }
            case SPHERE -> {
                for(Entity check : candidates) {
                    if (check.getPos().squaredDistanceTo(center) <= radius * radius) {
                        entities.add(check);
                    }
                }
            }
            case PRISM ->  {
                for(Entity check : candidates) {
                    Vec3d pos = check.getPos();
                    if(Math.abs(pos.y - center.y) <= height/2) {
                        entities.add(check);
                    }
                }
            }
            case CYLINDER -> {
                for(Entity check : candidates) {
                    Vec3d pos = check.getPos();
                    if(pos.squaredDistanceTo(center.x, pos.y, center.z) <= radius * radius &&
                            Math.abs(pos.y - center.y) <= height/2
                    ) {
                        entities.add(check);
                    }
                }
            }
            default -> {
                return entities;
            }
        }

        return entities;
    }

    @Inject(method = "action", at = @At("HEAD"), cancellable = true)
    private static void shuffleAllTarget(SerializableData.Instance data, Entity entity, CallbackInfo ci){
        Consumer<Pair<Entity, Entity>> bientityAction = data.get("bientity_action");
        Predicate<Pair<Entity, Entity>> bientityCondition = data.get("bientity_condition");
        Consumer<Entity> targetAction = data.get("target_action");
        Consumer<Entity> selfAction = data.get("self_action");
        boolean includeTarget = data.get("include_target");
        double radius = data.get("radius");
        double height = data.getDouble("height");
        int maxTarget = MathUtil.nonNegative(data.getInt("max_target"));
        AreaShape shape = AreaShape.fromName(data.getString("shape"));

        List<Entity> allTargets = new ArrayList<>(getEntitiesInShape(
                shape,
                entity,
                radius,
                height,
                includeTarget
        ).stream().filter(check -> bientityCondition == null || bientityCondition.test(new Pair<>(entity, check)))
                .toList()
        );

        if (maxTarget <= 0) {
            Collections.shuffle(allTargets);
        }

        int count = 0;
        for (Entity target : allTargets) {
            targetAction.accept(target);
            bientityAction.accept(new Pair<>(entity, target));
            count++;
            if (maxTarget > 0 && count >= maxTarget)
                break;
        }
        selfAction.accept(entity);

        ci.cancel();
    }

@Inject(method = "getFactory", at = @At("RETURN"), cancellable = true, remap = false)
    private static void addMaxTarget(CallbackInfoReturnable<ActionFactory<Entity>> cir){
        cir.setReturnValue(
                new ActionFactory<>(
                        Apoli.identifier("area_of_effect"),
                        new SerializableData()
                                .add("radius", SerializableDataTypes.DOUBLE, 16d)
                                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                                .add("include_target", SerializableDataTypes.BOOLEAN, false)
                                .add("max_target",SerializableDataTypes.INT, 0)
                                .add("target_action", ApoliDataTypes.ENTITY_ACTION, null)
                                .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                                .add("shape", SerializableDataTypes.STRING, AreaShape.CUBE.getName())
                                .add("height", SerializableDataTypes.DOUBLE, 4d)
                        ,
                        AreaOfEffectAction::action
                )
        );
    }
}
