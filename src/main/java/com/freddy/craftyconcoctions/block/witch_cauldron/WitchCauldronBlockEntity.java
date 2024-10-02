package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
    public int ticksSinceModeSwitch = 0;

    boolean initialMarkDirtyCalled = false; // otherwise renderer won't show anything until something changes

    private void switchModeTo(int newMode)
    {
        mode = newMode;
        ticksSinceModeSwitch = 0;
        markDirty();
    }

    /* ------ DATA STORAGE & SYNC ------ */

    @Override
    public void markDirty()
    {
        if (world == null || world.isClient)
            return;
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos()))
            ServerPlayNetworking.send(player, new S2CWitchCauldronSyncPayload(getPos(), mode, waterAmount));
        super.markDirty();
    }
    // used by client to update data from server
    public void setData(int mode, int waterAmount)
    {
        this.mode = mode;
        this.waterAmount = waterAmount;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        super.writeNbt(nbt, registryLookup);

        nbt.putInt("mode", mode);
        nbt.putInt("waterAmount", waterAmount);
    }
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        mode = nbt.getInt("mode");
        waterAmount = nbt.getInt("waterAmount");

        markDirty();

        super.readNbt(nbt, registryLookup);
    }

    /* ------ FUNCTIONALITY ------ */

    @SuppressWarnings("unused")
    public void tick(World world, BlockPos pos, BlockState state)
    {
        ticksSinceModeSwitch++;
        if (!initialMarkDirtyCalled)
        {
            markDirty();
            initialMarkDirtyCalled = true;
        }
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
            glassBottleHasWater = heldStack.get(DataComponentTypes.POTION_CONTENTS).matches(Potions.WATER);

        switch (mode)
        {
            case 0:
                // putting water inside
                if (waterAmount < 3)
                {
                    if (holdingWaterBucket)
                    {
                        waterAmount = 3; markDirty();
                        interactionOccurred = true;
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                        player.giveItemStack(Items.BUCKET.getDefaultStack());
                    }
                    else if (holdingGlassBottle && glassBottleHasWater)
                    {
                        waterAmount = waterAmount + 1; markDirty();
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
                        waterAmount = 0; markDirty();
                    }
                    else if (holdingGlassBottle && !glassBottleHasWater)
                    {
                        waterAmount = waterAmount - 1; markDirty();
                        interactionOccurred = true;
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                        player.giveItemStack(PotionContentsComponent.createStack(Items.POTION, Potions.WATER));
                    }
                }
        }

        return interactionOccurred;
    }
}
