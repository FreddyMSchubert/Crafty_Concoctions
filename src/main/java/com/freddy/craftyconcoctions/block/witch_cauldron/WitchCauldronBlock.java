package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WitchCauldronBlock extends BlockWithEntity
{

    private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), createCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)), BooleanBiFunction.ONLY_FIRST);

    public static final MapCodec<WitchCauldronBlock> CODEC = WitchCauldronBlock.createCodec(WitchCauldronBlock::new);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public WitchCauldronBlock(Settings settings)
    { super(settings); }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec()
    {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return SHAPE;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return new WitchCauldronBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
    {
        return validateTicker(type, ModBlockEntities.WITCH_CAULDRON_BE, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit)
    {
        if (world.isClient)
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WitchCauldronBlockEntity)
        {
            boolean interactionOccurred = ((WitchCauldronBlockEntity) blockEntity).onUse(player);
            if (interactionOccurred)
                return ActionResult.CONSUME;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
    {
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WitchCauldronBlockEntity witchCauldronBlockEntity)
                witchCauldronBlockEntity.scatterIngredients(pos, world);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
