package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;

import java.util.List;

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

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type)
    {
        if (CraftyConcoctions.DEBUG)
            getDebugTooltip(stack, tooltip);
        super.appendTooltip(stack, context, tooltip, type);
    }

    private void getDebugTooltip(ItemStack stack, List<Text> tooltipOutput)
    {
        if (!stack.contains(DataComponentTypes.FOOD)) return;
        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
        if (foodComponent.effects().isEmpty()) return;

        for (FoodComponent.StatusEffectEntry entry : foodComponent.effects())
        {
            StatusEffectInstance effect = entry.effect();
            tooltipOutput.add(Text.translatable(effect.getTranslationKey()).append(Text.of(" " + effect.getAmplifier() + " " + effect.getDuration())));
        }
    }
}
