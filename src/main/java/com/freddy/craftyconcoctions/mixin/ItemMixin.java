package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.item.ModItemTags;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin
{
    @Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At("HEAD"), cancellable = true)
    private void modifyPotionName(ItemStack stack, CallbackInfoReturnable<Text> cir)
    {
        if (stack.contains(ModDataComponentTypes.POTION_COLOR)) // => mod potion
            cir.setReturnValue(getDefaultName(stack));

        if (stack.getItem().getDefaultStack().isIn(ModItemTags.POTION_DEFINERS) && stack.getRarity() == Rarity.COMMON) // dont overwrite rarity info
            cir.setReturnValue(Text.translatable(stack.getTranslationKey()).withColor(13022207));
    }

    @Unique
    private Text getDefaultName(ItemStack stack)
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
