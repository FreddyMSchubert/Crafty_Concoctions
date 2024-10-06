package com.freddy.craftyconcoctions.block;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags
{
    // utilities
    public static final TagKey<Block> HEATING_BLOCKS_LVL_2 =
            createTag("heating_blocks_lvl_2");
    public static final TagKey<Block> HEATING_BLOCKS_LVL_1 =
            createTag("heating_blocks_lvl_1");

    private static TagKey<Block> createTag(String name)
    {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(CraftyConcoctions.MOD_ID, name));
    }
}
