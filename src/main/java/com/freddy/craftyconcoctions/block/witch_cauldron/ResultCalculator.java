package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.effect.ModEffects;
import com.freddy.craftyconcoctions.item.ModItems;
import com.freddy.craftyconcoctions.util.Color;
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

public class ResultCalculator
{
    public static ResultCalculatorOutput getResult(List<Item> ingredients)
    {
        ItemStack stack = new ItemStack(ModItems.WITCH_POTION);
        stack.set(DataComponentTypes.FOOD, new FoodComponent(0, 0f, true, 2f, Optional.ofNullable(Items.GLASS_BOTTLE.getDefaultStack()), Arrays.asList(new FoodComponent.StatusEffectEntry(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, 1), 1f))));
        return new ResultCalculatorOutput(stack, new Color(255, 125, 75));
    }

    private static final List<EffectPair> effectPairs = Arrays.asList( // fermented spider eyes are placeholders for when i dont know yet // make this use lists for multiple options // and add goal color tones
            new EffectPair(new StatusEffectInstance(StatusEffects.SPEED, 200, 1), new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1), Items.SUGAR, Items.TURTLE_SCUTE),
            new EffectPair(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1), new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1), Items.BLAZE_POWDER, Items.FERMENTED_SPIDER_EYE),
            new EffectPair(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 1), new StatusEffectInstance(ModEffects.JUMP_REDUCTION, 200, 1), Items.RABBIT_FOOT, Items.FERMENTED_SPIDER_EYE),
            new EffectPair(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1), new StatusEffectInstance(StatusEffects.POISON, 200, 1), Items.GHAST_TEAR, Items.PUFFERFISH),
            new EffectPair(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 1), new StatusEffectInstance(ModEffects.FIRE_WEAKNESS, 200, 1), Items.MAGMA_CREAM, Items.PAPER),
            new EffectPair(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 200, 1), new StatusEffectInstance(ModEffects.BREATHLESSNESS, 200, 1), Items.FERMENTED_SPIDER_EYE, Items.ENDER_PEARL),
            new EffectPair(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 1), new StatusEffectInstance(StatusEffects.GLOWING, 200, 1), Items.LIGHT_GRAY_STAINED_GLASS_PANE, Items.GLOW_BERRIES),
            new EffectPair(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 1), new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 1), Items.GLOW_INK_SAC, Items.SPIDER_EYE),
            new EffectPair(new StatusEffectInstance(StatusEffects.SATURATION, 200, 1), new StatusEffectInstance(StatusEffects.HUNGER, 200, 1), Items.GOLDEN_CARROT, Items.ROTTEN_FLESH),
            new EffectPair(new StatusEffectInstance(StatusEffects.LEVITATION, 200, 1), new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 1), Items.SHULKER_SHELL, Items.FEATHER),
            new EffectPair(new StatusEffectInstance(StatusEffects.LUCK, 200, 1), new StatusEffectInstance(StatusEffects.UNLUCK, 200, 1), Items.FERMENTED_SPIDER_EYE, Items.FERMENTED_SPIDER_EYE),
            new EffectPair(new StatusEffectInstance(StatusEffects.OOZING, 200, 1), new StatusEffectInstance(StatusEffects.INFESTED, 200, 1), Items.SLIME_BALL, Items.STONE),
            new EffectPair(new StatusEffectInstance(StatusEffects.WIND_CHARGED, 200, 1), new StatusEffectInstance(StatusEffects.WEAVING, 200, 1), Items.WIND_CHARGE, Items.COBWEB)
    );

    public static class ResultCalculatorOutput
    {
        ItemStack output;
        Color color;

        ResultCalculatorOutput(ItemStack output, Color color)
        {
            this.output = output;
            this.color = color;
        }
    }

    static class EffectPair
    {
        StatusEffectInstance effect1;
        StatusEffectInstance effect2;
        Item item1;
        Item item2;

        EffectPair(StatusEffectInstance effect1, StatusEffectInstance effect2, Item item1, Item item2)
        {
            this.effect1 = effect1;
            this.effect2 = effect2;
            this.item1 = item1;
            this.item2 = item2;
        }
    }
}
