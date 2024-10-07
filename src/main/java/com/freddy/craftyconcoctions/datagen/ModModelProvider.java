package com.freddy.craftyconcoctions.datagen;

import com.freddy.craftyconcoctions.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider
{
    public ModModelProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator)
    {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator)
    {
        itemModelGenerator.register(ModItems.CLOVER_LEAVES_2, Models.GENERATED);
        itemModelGenerator.register(ModItems.CLOVER_LEAVES_3, Models.GENERATED);
        itemModelGenerator.register(ModItems.CLOVER_LEAVES_4, Models.GENERATED);
        itemModelGenerator.register(ModItems.LAPIS_LAZULI_DUST, Models.GENERATED);
    }
}
