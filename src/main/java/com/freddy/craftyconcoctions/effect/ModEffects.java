package com.freddy.craftyconcoctions.effect;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.effect.custom.BreathlessnessStatusEffect;
import com.freddy.craftyconcoctions.effect.custom.FireWeaknessStatusEffect;
import com.freddy.craftyconcoctions.effect.custom.JumpReductionStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects
{
    public static void register() {}

    public static final RegistryEntry<StatusEffect> JUMP_REDUCTION = register("jump_reduction", new JumpReductionStatusEffect(StatusEffectCategory.HARMFUL, 0x00FF00).addAttributeModifier(EntityAttributes.GENERIC_JUMP_STRENGTH, Identifier.of(CraftyConcoctions.MOD_ID, "effect.jump_reduction"), -0.05, EntityAttributeModifier.Operation.ADD_VALUE));
    public static final RegistryEntry<StatusEffect> FIRE_WEAKNESS = register("fire_weakness", new FireWeaknessStatusEffect(StatusEffectCategory.HARMFUL, 0xFF0000));
    public static final RegistryEntry<StatusEffect> BREATHLESSNESS = register("breathlessness", new BreathlessnessStatusEffect(StatusEffectCategory.HARMFUL, 0x00FF00));


    public static RegistryEntry<StatusEffect> register(String id, StatusEffect effect)
    {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(CraftyConcoctions.MOD_ID, id), effect);
    }
}
