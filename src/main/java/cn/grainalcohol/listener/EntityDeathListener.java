package cn.grainalcohol.listener;

import cn.grainalcohol.power.ActionOnDeathPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class EntityDeathListener implements ServerLivingEntityEvents.AfterDeath{
    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new EntityDeathListener());
    }

    @Override
    public void afterDeath(LivingEntity entity, DamageSource source) {
        PowerHolderComponent.getPowers(entity, ActionOnDeathPower.class).forEach(power -> {
            if (power.shouldApply(source, 0, getAttacker(source))) {
                power.apply();
            }
        });
    }

    private LivingEntity getAttacker(DamageSource source) {
        return source.getAttacker() instanceof LivingEntity ? (LivingEntity) source.getAttacker() : null;
    }
}
