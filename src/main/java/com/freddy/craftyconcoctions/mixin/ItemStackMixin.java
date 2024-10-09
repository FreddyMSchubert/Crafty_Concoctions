package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.item.ModItemTags;
import com.freddy.craftyconcoctions.screen.PotionNameEditScreen;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @Inject(method = "use", at = @At("HEAD"))
    private void injectOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
    {
        if (!world.isClient || !user.isSneaking()) return;
        ItemStack stack = user.getStackInHand(hand);
        Item item = stack.getItem();
        if (!(item instanceof PotionItem) || !stack.contains(ModDataComponentTypes.POTION_COLOR)) return; // check whether its a custom mod potion
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        client.setScreen(new PotionNameEditScreen());
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void injectGetName(CallbackInfoReturnable<Text> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;
        ItemStack defaultStack = stack.getItem().getDefaultStack();
        if (defaultStack.isIn(ModItemTags.POTION_DEFINERS) || defaultStack.isIn(ModItemTags.GOODNESS_DEFINERS) || defaultStack.isIn(ModItemTags.MODIFIERS))
        {
            Text text = stack.get(DataComponentTypes.CUSTOM_NAME);
            if (text != null) {
                cir.setReturnValue(Text.of(text.getString() + " \uD83E\uDDC9"));
            } else {
                Text text2 = stack.get(DataComponentTypes.ITEM_NAME);
                cir.setReturnValue(Text.of(Text.of(text2 != null ? text2 : stack.getItem().getName(stack)).getString() + " \uD83E\uDDC9"));
            }
        }
    }
}
