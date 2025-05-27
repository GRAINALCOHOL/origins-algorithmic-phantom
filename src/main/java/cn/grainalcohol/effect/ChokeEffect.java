package cn.grainalcohol.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.DyeColor;

import java.util.Objects;

import static cn.grainalcohol.init.OAPDamageSource.CHOKING;
import static cn.grainalcohol.util.ColorUtil.getIntFromDyeColor;

public class ChokeEffect extends StatusEffect {
    private static final int INTERVAL = 10;

    public ChokeEffect() {
        super(StatusEffectCategory.HARMFUL, getIntFromDyeColor(DyeColor.BROWN));
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient) {
            int elapsedTicks = Objects.requireNonNull(entity.getStatusEffect(this)).getDuration();
            if (elapsedTicks % INTERVAL == 0) {
                entity.damage(entity.getDamageSources().create(CHOKING),1.0F+amplifier*0.5F);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
