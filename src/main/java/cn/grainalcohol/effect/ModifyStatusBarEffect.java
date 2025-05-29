package cn.grainalcohol.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.DyeColor;

import static cn.grainalcohol.util.ColorUtil.getIntFromDyeColor;

public class ModifyStatusBarEffect extends StatusEffect {
    protected ModifyStatusBarEffect() {
        super(StatusEffectCategory.HARMFUL, getIntFromDyeColor(DyeColor.GRAY));
    }

    //WIP
}
