package com.freddy.craftyconcoctions.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class FireWeaknessStatusEffect extends StatusEffect
{
    public FireWeaknessStatusEffect(StatusEffectCategory category, int color)
    {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        return true;
    }

    private float getIncreasedDamage(float amount, int amplifier)
    {
        return amount * (amplifier + 1.5F);
    }

    @Override
    public void onEntityDamage(LivingEntity entity, int amplifier, DamageSource source, float amount)
    {
        Optional<RegistryEntry.Reference<DamageType>> optionalDamageType = entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DamageTypes.GENERIC);
        if (optionalDamageType.isEmpty())
            return;
        RegistryEntry<DamageType> damageTypeEntry = optionalDamageType.get();
        if (!source.getType().toString().toLowerCase().contains("fire"))
            return;
        entity.damage(new DamageSource(damageTypeEntry), getIncreasedDamage(amount, amplifier));

        super.onEntityDamage(entity, amplifier, source, amount);
    }
}
