package cn.grainalcohol.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.DyeColor;

import static cn.grainalcohol.util.ColorUtil.getIntFromDyeColor;

public class FrozenEffect extends StatusEffect {
    public FrozenEffect() {
        super(StatusEffectCategory.HARMFUL, getIntFromDyeColor(DyeColor.LIGHT_BLUE));
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.canFreeze()){
            entity.inPowderSnow = true;
            entity.setFrozenTicks(140);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
