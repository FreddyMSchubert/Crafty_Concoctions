package com.freddy.craftyconcoctions.effect.custom;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ModStatusEffect extends StatusEffect
{
    public ModStatusEffect(StatusEffectCategory category, int color)
    {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        return true;
    }
}
