package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronSettings;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(PotionItem.class)
public class PotionItemMixin
{
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void injectAppendtooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci)
    {
        if (stack.contains(DataComponentTypes.POTION_CONTENTS) && stack.get(DataComponentTypes.POTION_CONTENTS).hasEffects())
            stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().forEach((effect) -> tooltip.add(Text.of(effect.toString())));

        if (CraftyConcoctions.DEBUG)
        {
            getDebugTooltip(stack, tooltip);
            ci.cancel();
        }
    }

    @Unique
    private void getDebugTooltip(ItemStack stack, List<Text> tooltipOutput)
    {
        if (!stack.contains(DataComponentTypes.FOOD)) return;
        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
        if (foodComponent.effects().isEmpty()) return;

        for (FoodComponent.StatusEffectEntry entry : foodComponent.effects())
        {
            StatusEffectInstance effect = entry.effect();
            tooltipOutput.add(Text.translatable(effect.getTranslationKey()).append(Text.of(" " + (effect.getAmplifier() + 1) + " " + effect.getDuration())));
        }

        if (!stack.contains(ModDataComponentTypes.POTION_INGREDIENTS)) return;
        NbtCompound ingredients = stack.get(ModDataComponentTypes.POTION_INGREDIENTS);
        Map<Item, Integer> items = new HashMap<>();
        for (int i = 0; i < WitchCauldronSettings.MAX_INGREDIENTS; i++)
        {
            if (ingredients.contains("ingredient" + i))
            {
                Item item = Registries.ITEM.get(Identifier.of(ingredients.getString("ingredient" + i)));
                if (items.containsKey(item))
                    items.put(item, items.get(item) + 1);
                else
                    items.put(item, 1);
            }
            else
                break;
        }
        for (Map.Entry<Item, Integer> entry : items.entrySet())
            tooltipOutput.add(Text.of(entry.getValue() + "x " + entry.getKey().getName().getString()));
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void injectGetMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir)
    {
        if (stack.contains(ModDataComponentTypes.POTION_CONSUME_TIME))
            cir.setReturnValue(stack.get(ModDataComponentTypes.POTION_CONSUME_TIME));
    }

}
