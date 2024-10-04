package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.effect.ModEffects;
import com.freddy.craftyconcoctions.item.ModItemTags;
import com.freddy.craftyconcoctions.item.ModItems;
import com.freddy.craftyconcoctions.util.Color;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import oshi.util.tuples.Pair;

import java.util.*;

public class ResultCalculator
{
    public static ResultCalculatorOutput getResult(List<Item> ingredients)
    {
        FoodComponent food = new FoodComponent(0, 0f, true, 2f, Optional.ofNullable(Items.GLASS_BOTTLE.getDefaultStack()), new ArrayList<>());
        Color color;

        boolean isGoodPotion;
        boolean isBadPotion;
        boolean isMundanePotion = false;
        boolean isAwkwardPotion = false;
        boolean isThickPotion = false;
        boolean isDilutedPotion = false;

        // Sort ingredients into groups

        List<Item> potionDefiners = ingredients.stream().filter(item -> item.getDefaultStack().isIn(ModItemTags.POTION_DEFINERS)).toList();
        List<Item> modifiers = ingredients.stream().filter(item -> item.getDefaultStack().isIn(ModItemTags.MODIFIERS)).toList();
        List<Item> goodnessDefiners = ingredients.stream().filter(item -> item.getDefaultStack().isIn(ModItemTags.GOODNESS_DEFINERS)).toList();

        for (Item item : ingredients)
        {
            if (!potionDefiners.contains(item) && !modifiers.contains(item) && !goodnessDefiners.contains(item))
            {
                isDilutedPotion = true;
                break;
            }
        }

        // Store effect modifier counts

        int redstoneCount = 0;
        int glowstoneCount = 0;
        for (Item item : modifiers)
        {
            if (item == Items.REDSTONE)
                redstoneCount++;
            else if (item == Items.GLOWSTONE_DUST)
                glowstoneCount++;
        }

        // Determine goodness

        int goodness = 0;
        for (Item item : goodnessDefiners)
        {
            if (item == Items.NETHER_WART)
                goodness += 1;
            else if (item == Items.FERMENTED_SPIDER_EYE)
                goodness -= 1;
        }
        isGoodPotion = goodness > 0;
        isBadPotion = goodness < 0;

        if (goodness == 0)
            isMundanePotion = true;

        // Missing potion definers or goodness definers prefixes

        if (potionDefiners.isEmpty() || goodnessDefiners.isEmpty())
        {
            // Mundane, thick, or awkward potion
            color = WitchCauldronSettings.WATER_COLOR.copy();
            food = new FoodComponent(0, 0f, true, 2f, Optional.ofNullable(Items.GLASS_BOTTLE.getDefaultStack()), new ArrayList<>());

            if (potionDefiners.isEmpty())
                isAwkwardPotion = true;
            if (goodnessDefiners.isEmpty())
                isThickPotion = true;

            return getResult(food, color, ingredients, isGoodPotion, isBadPotion, isMundanePotion, isAwkwardPotion, isThickPotion, isDilutedPotion);
        }

        // Determine effects

        List<StatusEffectInstance> effects = new ArrayList<>();
        List<Color> colors = new ArrayList<>();

        for (Item potionDefiner : potionDefiners)
        {
            for (Pair<EffectData, EffectData> pair : effectPairs)
            {
                StatusEffectInstance effect;
                Color effectColor;
                int maxAmplifier;

                if (isGoodPotion && pair.getA().item.contains(potionDefiner))
                {
                    effect = pair.getA().effect;
                    effectColor = pair.getA().color;
                    maxAmplifier = pair.getA().maxAmplifier;
                }
                else if (isBadPotion && pair.getB().item.contains(potionDefiner))
                {
                    effect = pair.getB().effect;
                    effectColor = pair.getB().color;
                    maxAmplifier = pair.getB().maxAmplifier;
                }
                else
                    continue;

                // strength

                int duration;
                int amplifier;

                duration = effect.getDuration() * (redstoneCount + 1);
                if (glowstoneCount == 1)
                    amplifier = Math.round((float) maxAmplifier / 2) == 1 && maxAmplifier != 1 ? 2 : Math.round((float) maxAmplifier / 2);
                else if (glowstoneCount > 1)
                    amplifier = maxAmplifier;
                else
                    amplifier = 1;

                StatusEffectInstance newEffect = new StatusEffectInstance(effect.getEffectType(), duration, amplifier);
                effects.add(newEffect);

                // color

                int assumedDuration = effect.getDuration() * 2;
                int assumedAmplifier = Math.round((float) maxAmplifier / 2);

                float durationFactor = Math.min(1.0f, (float) duration / assumedDuration);
                float amplifierFactor = Math.min(1.0f, (float) amplifier / assumedAmplifier);
                float powerFactor = (durationFactor + amplifierFactor) / 2.0f;

                Color newColor = effectColor.copy();
                newColor.red((int) (newColor.RED + (255 - newColor.RED) * powerFactor));
                newColor.green((int) (newColor.GREEN + (255 - newColor.GREEN) * powerFactor));
                newColor.blue((int) (newColor.BLUE + (255 - newColor.BLUE) * powerFactor));
                colors.add(newColor);
            }
        }

        // return merged effects & colors

        for (StatusEffectInstance instance : effects)
            food.effects().add(new FoodComponent.StatusEffectEntry(instance, 1f));
        color = Color.blendColors(colors);

        return getResult(food, color, ingredients, isGoodPotion, isBadPotion, isMundanePotion, isAwkwardPotion, isThickPotion, isDilutedPotion);
    }

    public static ResultCalculatorOutput getResult(FoodComponent food, Color color, List<Item> ingredients, boolean good, boolean bad, boolean mundane, boolean awkward, boolean thick, boolean diluted)
    {
        ItemStack stack = new ItemStack(ModItems.WITCH_POTION);
        color.alpha(150);
        stack.set(DataComponentTypes.FOOD, food);
        stack.set(ModDataComponentTypes.POTION_COLOR, color.getArgbInt());
        stack.set(ModDataComponentTypes.GOOD_POTION, good);
        stack.set(ModDataComponentTypes.BAD_POTION, bad);
        stack.set(ModDataComponentTypes.MUNDANE_POTION, mundane);
        stack.set(ModDataComponentTypes.AWKWARD_POTION, awkward);
        stack.set(ModDataComponentTypes.THICK_POTION, thick);
        stack.set(ModDataComponentTypes.DILUTED_POTION, diluted);
        stack.set(ModDataComponentTypes.POTION_INGREDIENTS, getIngredientsAsNbtCompound(ingredients));
        return new ResultCalculatorOutput(stack, color);
    }

    public static NbtCompound getIngredientsAsNbtCompound(List<Item> ingredients)
    {
        NbtCompound nbt = new NbtCompound();
        for (int i = 0; i < ingredients.size(); i++)
            nbt.putString("ingredient" + i, ingredients.get(i).toString());
        return nbt;
    }

    /*
        Durations are values for no redstone input
        Amplifiers are values for 1 glowstone input (no glowstone will always be level 1)
     */
    private static final List<Pair<EffectData, EffectData>> effectPairs = Arrays.asList(
            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.SPEED, 20 * 180, 1), 2, Collections.singletonList(Items.SUGAR), new Color(51, 235, 255)),
                    new EffectData(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 90, 1), 4, Collections.singletonList(Items.TURTLE_SCUTE), new Color(139, 175, 224))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 180, 1), 2, Collections.singletonList(Items.BLAZE_POWDER), new Color(255, 199, 9)),
                    new EffectData(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 90, 1), 1, Collections.singletonList(Items.FERMENTED_SPIDER_EYE), new Color(72, 77, 72))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 180, 1), 4, Collections.singletonList(Items.RABBIT_FOOT), new Color(253, 255, 132)),
                    new EffectData(new StatusEffectInstance(ModEffects.JUMP_REDUCTION, 20 * 90, 1), 2, Collections.singletonList(Items.FERMENTED_SPIDER_EYE), new Color(251, 30, 255))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 45, 1), 2, Collections.singletonList(Items.GHAST_TEAR), new Color(205, 92, 171)),
                    new EffectData(new StatusEffectInstance(StatusEffects.POISON, 20 * 45, 1), 2, Collections.singletonList(Items.PUFFERFISH), new Color(135, 163, 99))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 180, 1), 1, Collections.singletonList(Items.MAGMA_CREAM), new Color(255, 153, 0)),
                    new EffectData(new StatusEffectInstance(ModEffects.FIRE_WEAKNESS, 20 * 90, 1), 2, Collections.singletonList(Items.PAPER), new Color(153, 92, 0))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 20 * 90, 1), 1, Collections.singletonList(Items.FERMENTED_SPIDER_EYE), new Color(152, 218, 192)),
                    new EffectData(new StatusEffectInstance(ModEffects.BREATHLESSNESS, 20 * 45, 1), 3, Collections.singletonList(Items.ENDER_PEARL), new Color(77, 191, 146))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20 * 180, 1), 1, Collections.singletonList(Items.LIGHT_GRAY_STAINED_GLASS_PANE), new Color(246, 246, 246)),
                    new EffectData(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 120, 1), 1, Collections.singletonList(Items.GLOW_BERRIES), new Color(148, 160, 97))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 180, 1), 1, Collections.singletonList(Items.GLOW_INK_SAC), new Color(194, 255, 102)),
                    new EffectData(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * 45, 1), 1, Collections.singletonList(Items.SPIDER_EYE), new Color(31, 31, 35))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 180, 1), 2, Collections.singletonList(Items.GOLDEN_CARROT), new Color(248, 36, 35)),
                    new EffectData(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 45, 1), 1, Collections.singletonList(Items.ROTTEN_FLESH), new Color(88, 118, 83))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.LEVITATION, 20 * 30, 1), 2, Collections.singletonList(Items.SHULKER_SHELL), new Color(206, 255, 255)),
                    new EffectData(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 90, 1), 1, Collections.singletonList(Items.FEATHER), new Color(243, 207, 185))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.LUCK, 20 * 300, 1), 1, Collections.singletonList(Items.FERMENTED_SPIDER_EYE), new Color(89, 193, 6)),
                    new EffectData(new StatusEffectInstance(StatusEffects.UNLUCK, 20 * 300, 1), 1, Collections.singletonList(Items.FERMENTED_SPIDER_EYE), new Color(192, 164, 77))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.OOZING, 20 * 180, 1), 1, Collections.singletonList(Items.SLIME_BALL), new Color(153, 255, 163)),
                    new EffectData(new StatusEffectInstance(StatusEffects.INFESTED, 20 * 180, 1), 1, Collections.singletonList(Items.STONE), new Color(140, 155, 140))),

            new Pair<>(new EffectData(new StatusEffectInstance(StatusEffects.WIND_CHARGED, 20 * 180, 1), 1, Collections.singletonList(Items.WIND_CHARGE), new Color(198, 201, 255)),
                    new EffectData(new StatusEffectInstance(StatusEffects.WEAVING, 20 * 180, 1), 1, Collections.singletonList(Items.COBWEB), new Color(120, 105, 90)))
    );

    public static class ResultCalculatorOutput
    {
        ItemStack output;
        Color color;

        ResultCalculatorOutput(ItemStack output, Color color)
        {
            // CraftyConcoctions.LOGGER.info("ResultCalculatorOutput: {} - {}; {}", output, color.getRGBA(), output.getComponents());
            this.output = output;
            this.color = color;
        }
    }

    static class EffectData
    {
        StatusEffectInstance effect;
        int maxAmplifier;
        List<Item> item;
        Color color;

        EffectData(StatusEffectInstance effect, int maxAmplifier, List<Item> item, Color color)
        {
            this.effect = effect;
            this.maxAmplifier = maxAmplifier;
            this.item = item;
            this.color = color;
        }
    }
}
