package com.freddy.craftyconcoctions.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BreathlessnessStatusEffect extends StatusEffect
{
    public BreathlessnessStatusEffect(StatusEffectCategory category, int color)
    {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier)
    {
        return super.applyUpdateEffect(entity, amplifier);
    }
}
