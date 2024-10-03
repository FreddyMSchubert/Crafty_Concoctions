package com.freddy.craftyconcoctions.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.UseAction;

public class WitchPotionItem extends Item
{
    public WitchPotionItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public SoundEvent getEatSound()
    { return SoundEvents.ENTITY_WITCH_DRINK; }
    @Override
    public UseAction getUseAction(ItemStack stack)
    { return UseAction.DRINK; }
}
