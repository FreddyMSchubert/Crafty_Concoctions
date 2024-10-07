package com.freddy.craftyconcoctions.datagen;

import com.freddy.craftyconcoctions.block.witch_cauldron.ResultCalculator;
import com.freddy.craftyconcoctions.item.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import oshi.util.tuples.Pair;

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
        for (Pair<ResultCalculator.EffectData, ResultCalculator.EffectData> pair : ResultCalculator.getEffectPairs())
        {
            for (Item item : pair.getA().getItems())
                getOrCreateTagBuilder(ModItemTags.POTION_DEFINERS)
                        .add(item);
            for (Item item : pair.getB().getItems())
                getOrCreateTagBuilder(ModItemTags.POTION_DEFINERS)
                        .add(item);
        }
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
