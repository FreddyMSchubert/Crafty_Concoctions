package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntityRenderer;
import com.freddy.craftyconcoctions.item.ModItems;
import com.freddy.craftyconcoctions.networking.ModMessagesClient;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.math.ColorHelper;

public class CraftyConcoctionsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModMessagesClient.registerClient();

        BlockEntityRendererFactories.register(ModBlockEntities.WITCH_CAULDRON_BE, WitchCauldronBlockEntityRenderer::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ColorHelper.Argb.fullAlpha(stack.getOrDefault(ModDataComponentTypes.POTION_COLOR, 0xFF00FFFF)), ModItems.WITCH_POTION);
    }
}
