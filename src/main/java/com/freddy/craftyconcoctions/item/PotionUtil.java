package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PotionUtil
{
    public static Text getDefaultName(ItemStack stack)
    {
        MutableText name = Text.translatable("item.minecraft.potion");

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

        if (stack.contains(ModDataComponentTypes.STRONG_POTION) && stack.get(ModDataComponentTypes.STRONG_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.strong").append(" ").append(name);

        if (stack.contains(ModDataComponentTypes.DILUTED_POTION) && stack.get(ModDataComponentTypes.DILUTED_POTION))
            name = Text.translatable("item.craftyconcoctions.potion_type.diluted").append(" ").append(name);

        return name;
    }
}
