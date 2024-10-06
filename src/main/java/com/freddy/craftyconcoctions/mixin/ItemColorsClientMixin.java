package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemColors.class)
public class ItemColorsClientMixin
{

    @Redirect(method = "create",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/color/item/ItemColors;register(Lnet/minecraft/client/color/item/ItemColorProvider;[Lnet/minecraft/item/ItemConvertible;)V"))
    private static void redirectPotionColorRegistration(ItemColors itemColors, ItemColorProvider provider, ItemConvertible... items)
    {
        for (ItemConvertible item : items)
        {
            if (item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.TIPPED_ARROW)
                itemColors.register((stack, tintIndex) -> {
                    if (tintIndex > 0)
                        return -1;
                    else if (stack.contains(ModDataComponentTypes.POTION_COLOR))
                        return stack.get(ModDataComponentTypes.POTION_COLOR);
                    else
                        return ColorHelper.Argb.fullAlpha(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getColor());
                }, item);
            else
                itemColors.register(provider, item);
        }
    }
}