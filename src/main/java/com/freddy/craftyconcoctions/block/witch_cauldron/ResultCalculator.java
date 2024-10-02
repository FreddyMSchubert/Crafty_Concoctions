package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// this is where the magic happens
// so far, no magic yet

public class ResultCalculator
{
    public static ItemStack getResult(List<Item> ingredients)
    {
        ItemStack stack = new ItemStack(ModItems.WITCH_POTION);
        stack.set(DataComponentTypes.FOOD, new FoodComponent(0, 0f, true, 2f, Optional.ofNullable(Items.GLASS_BOTTLE.getDefaultStack()), Arrays.asList(new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, 1), 1f))));
        return stack;
    }
}
