package com.freddy.craftyconcoctions.block;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WitchCauldronBlockEntity extends BlockEntity
{
    public WitchCauldronBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WITCH_CAULDRON_BE, pos, state);
    }

    public int mode = 0; // 0 = inputting water, 1 = inputting ingredients, 2 = brewing, 3 = outputting potion
    public int waterAmount = 0; // bucket = +3, bottle = +1; max = 3

    @SuppressWarnings("unused")
    public void tick(World world, BlockPos pos, BlockState state)
    {
    }

    // returns whether an interaction occurred
    public boolean onUse(PlayerEntity player)
    {
        boolean interactionOccurred = false;

        ItemStack heldStack = player.getStackInHand(Hand.MAIN_HAND);

        boolean holdingWaterBucket = heldStack.getItem() == Items.WATER_BUCKET;
        boolean holdingBucket = heldStack.getItem() == Items.BUCKET;
        boolean holdingGlassBottle = heldStack.getItem() == Items.POTION || heldStack.getItem() == Items.GLASS_BOTTLE;
        boolean glassBottleHasWater = false;
        if (holdingGlassBottle && heldStack.contains(DataComponentTypes.POTION_CONTENTS))
        {
            glassBottleHasWater = heldStack.get(DataComponentTypes.POTION_CONTENTS).matches(Potions.WATER);
            CraftyConcoctions.LOGGER.info("heldStack" + heldStack.get(DataComponentTypes.POTION_CONTENTS));
        }

        CraftyConcoctions.LOGGER.info("holdingWaterBucket: " + holdingWaterBucket + ", glassBottleHasWater: " + glassBottleHasWater);

        switch (mode)
        {
            case 0:
                // putting water inside
                if (waterAmount < 3)
                {
                    if (holdingWaterBucket)
                    {
                        waterAmount = 3;
                        interactionOccurred = true;
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                        player.giveItemStack(Items.BUCKET.getDefaultStack());
                    }
                    else if (holdingGlassBottle && glassBottleHasWater)
                    {
                        waterAmount = waterAmount + 1;
                        interactionOccurred = true;
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                        player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
                    }
                }

                // getting water back out
                if (waterAmount > 0)
                {
                    if (holdingBucket)
                    {
                        interactionOccurred = true;
                        if (waterAmount == 3)
                        {
                            heldStack.decrement(1);
                            player.setStackInHand(Hand.MAIN_HAND, heldStack);
                            player.giveItemStack(Items.WATER_BUCKET.getDefaultStack());
                        }
                        waterAmount = 0;
                    }
                    else if (holdingGlassBottle && !glassBottleHasWater)
                    {
                        waterAmount = waterAmount - 1;
                        interactionOccurred = true;
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                        player.giveItemStack(PotionContentsComponent.createStack(Items.POTION, Potions.WATER));
                    }
                }
        }

        CraftyConcoctions.LOGGER.info("mode: " + mode + ", waterAmount: " + waterAmount);

        return interactionOccurred;
    }
}
