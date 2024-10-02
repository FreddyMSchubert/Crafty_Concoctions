package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntityRenderer;
import com.freddy.craftyconcoctions.networking.ModMessagesClient;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CraftyConcoctionsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModMessagesClient.registerClient();

        BlockEntityRendererFactories.register(ModBlockEntities.WITCH_CAULDRON_BE, WitchCauldronBlockEntityRenderer::new);
    }
}
