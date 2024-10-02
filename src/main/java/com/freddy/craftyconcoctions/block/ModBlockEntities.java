package com.freddy.craftyconcoctions.block;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities
{
    public static BlockEntityType<WitchCauldronBlockEntity> WITCH_CAULDRON_BE;

    public static void register()
    {
        WITCH_CAULDRON_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "witch_cauldron_be"),
                BlockEntityType.Builder.create(WitchCauldronBlockEntity::new, ModBlocks.WITCH_CAULDRON).build(null));
    }
}
