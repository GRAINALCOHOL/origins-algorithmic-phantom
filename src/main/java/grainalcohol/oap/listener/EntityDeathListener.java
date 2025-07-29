package grainalcohol.oap.listener;

import grainalcohol.oap.power.ActionOnDeathPower;
import grainalcohol.oap.util.EntityUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class EntityDeathListener implements ServerLivingEntityEvents.AfterDeath{
    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new EntityDeathListener());
    }

    @Override
    public void afterDeath(LivingEntity entity, DamageSource source) {
        EntityUtil.getPowers(entity, ActionOnDeathPower.class, false)
                .stream()
                .filter(power -> power.shouldApply(source, 0, getAttacker(source)))
                .forEach(ActionOnDeathPower::apply);
    }

    private LivingEntity getAttacker(DamageSource source) {
        return source.getAttacker() instanceof LivingEntity ? (LivingEntity) source.getAttacker() : null;
    }
}
