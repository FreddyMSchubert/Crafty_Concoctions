package com.freddy.craftyconcoctions.datagen;

import com.freddy.craftyconcoctions.item.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider
{
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
    {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup)
    {
        // please check there isnt too much in here not actually used anymore
        getOrCreateTagBuilder(ModItemTags.POTION_DEFINERS)
                .add(Items.SPIDER_EYE)
                .add(Items.SUGAR)
                .add(Items.TURTLE_SCUTE)
                .add(Items.GHAST_TEAR)
                .add(Items.PAPER)
                .add(Items.ENDER_PEARL)
                .add(Items.RABBIT_FOOT)
                .add(Items.BLAZE_POWDER)
                .add(Items.GLISTERING_MELON_SLICE)
                .add(Items.GHAST_TEAR)
                .add(Items.PUFFERFISH)
                .add(Items.TURTLE_HELMET)
                .add(Items.MAGMA_CREAM)
                .add(Items.GOLDEN_CARROT)
                .add(Items.PHANTOM_MEMBRANE)
                .add(Items.BREEZE_ROD)
                .add(Items.COBWEB)
                .add(Items.SLIME_BLOCK)
                .add(Items.STONE);
        getOrCreateTagBuilder(ModItemTags.GOODNESS_DEFINERS)
                .add(Items.FERMENTED_SPIDER_EYE)
                .add(Items.NETHER_WART);
        getOrCreateTagBuilder(ModItemTags.MODIFIERS)
                .add(Items.REDSTONE)
                .add(Items.GLOWSTONE_DUST)
                .add(Items.DRAGON_BREATH)
                .add(Items.GUNPOWDER);
    }
}
