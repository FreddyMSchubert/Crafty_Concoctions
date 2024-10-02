package com.freddy.craftyconcoctions.block;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WitchCauldronBlockEntity extends BlockEntity
{
    public WitchCauldronBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WITCH_CAULDRON_BE, pos, state);
    }

    @SuppressWarnings("unused")
    public void tick(World world, BlockPos pos, BlockState state)
    {
        CraftyConcoctions.LOGGER.info("TICK!");
    }
}
