package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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

    @Override
    public Text getName(ItemStack stack)
    {
        return getDefaultName(stack);
    }

    private Text getDefaultName(ItemStack stack)
    {
        MutableText name = Text.translatable("item.craftyconcoctions.witch_potion");

        if (stack.contains(ModDataComponentTypes.MUNDANE_POTION) && stack.get(ModDataComponentTypes.MUNDANE_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.mundane").append(" ").append(name);
        if (stack.contains(ModDataComponentTypes.AWKWARD_POTION) && stack.get(ModDataComponentTypes.AWKWARD_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.awkward").append(" ").append(name);
        if (stack.contains(ModDataComponentTypes.THICK_POTION) && stack.get(ModDataComponentTypes.THICK_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.thick").append(" ").append(name);

        if (stack.contains(ModDataComponentTypes.GOOD_POTION) && stack.get(ModDataComponentTypes.GOOD_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.good").append(" ").append(name);
        if (stack.contains(ModDataComponentTypes.BAD_POTION) && stack.get(ModDataComponentTypes.BAD_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.bad").append(" ").append(name);

        if (stack.contains(ModDataComponentTypes.DILUTED_POTION) && stack.get(ModDataComponentTypes.DILUTED_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.diluted").append(" ").append(name);

        return name;
    }
}
