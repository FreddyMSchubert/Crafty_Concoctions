package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.item.ModItemTags;
import com.freddy.craftyconcoctions.item.PotionUtil;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin
{
    @Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At("HEAD"), cancellable = true)
    private void modifyPotionName(ItemStack stack, CallbackInfoReturnable<Text> cir)
    {
        if (stack.getItem().getDefaultStack().isIn(ModItemTags.POTION_DEFINERS) && stack.getRarity() == Rarity.COMMON) // dont overwrite rarity info
            cir.setReturnValue(Text.translatable(stack.getTranslationKey()).withColor(13022207));
        if (!stack.contains(ModDataComponentTypes.POTION_COLOR)) // => mod potion
            return;

        if (stack.contains(ModDataComponentTypes.CUSTOM_POTION_NAME) && !stack.get(ModDataComponentTypes.CUSTOM_POTION_NAME).isBlank())
            cir.setReturnValue(Text.of(stack.get(ModDataComponentTypes.CUSTOM_POTION_NAME)));
        else
            cir.setReturnValue(PotionUtil.getDefaultName(stack));
    }
}
