package com.freddy.craftyconcoctions.datagen;

import com.freddy.craftyconcoctions.block.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider
{
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup wrapperLookup)
    {
        // Utilities
        getOrCreateTagBuilder(ModBlockTags.HEATING_BLOCKS)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.LAVA)
                .add(Blocks.FIRE)
                .add(Blocks.SOUL_FIRE)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.SOUL_CAMPFIRE);
    }
}
