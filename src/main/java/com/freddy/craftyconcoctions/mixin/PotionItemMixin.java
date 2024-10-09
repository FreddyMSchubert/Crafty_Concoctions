package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.item.PotionUtil;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin
{
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void injectAppendtooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci)
    {
        if (CraftyConcoctions.DEBUG)
        {
            if (stack.contains(ModDataComponentTypes.CUSTOM_POTION_NAME) && !stack.get(ModDataComponentTypes.CUSTOM_POTION_NAME).isBlank())
                tooltip.add(Text.of(PotionUtil.getDefaultName(stack)));

            if (stack.contains(DataComponentTypes.POTION_CONTENTS) && stack.get(DataComponentTypes.POTION_CONTENTS).hasEffects())
                stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().forEach((effect) -> tooltip.add(Text.of(effect.toString())));
            ci.cancel();
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void injectGetMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir)
    {
        if (stack.contains(ModDataComponentTypes.POTION_CONSUME_TIME))
            cir.setReturnValue(stack.get(ModDataComponentTypes.POTION_CONSUME_TIME));
    }

}
