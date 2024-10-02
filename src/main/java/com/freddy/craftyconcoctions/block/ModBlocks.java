package com.freddy.craftyconcoctions.block;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks
{
    public static Block WITCH_CAULDRON;

    public static void register()
    {
        Identifier id = Identifier.of(CraftyConcoctions.MOD_ID, "witch_cauldron");
        Block block = new WitchCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAMPFIRE).nonOpaque().burnable().requiresTool().strength(2.0f));
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings().equipmentSlot((entity, stack) -> EquipmentSlot.HEAD))); // ITEM
        WITCH_CAULDRON = Registry.register(Registries.BLOCK, id, block); // BLOCK
    }
}
