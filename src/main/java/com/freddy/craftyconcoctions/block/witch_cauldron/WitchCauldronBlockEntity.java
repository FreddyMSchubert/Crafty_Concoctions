package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.block.ModBlockTags;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import com.freddy.craftyconcoctions.util.Color;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WitchCauldronBlockEntity extends BlockEntity
{
    public WitchCauldronBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.WITCH_CAULDRON_BE, pos, state);
    }

    public int mode = 0; // 0 = inputting water, 1 = inputting ingredients, 2 = brewing, 3 = outputting potion
    public int waterAmount = 0; // bucket = +3, bottle = +1; max = 3
    public int ticksSinceModeSwitch = 0;
    public Color currColor = WitchCauldronSettings.WATER_COLOR.copy();
    public Color goalColor = WitchCauldronSettings.WATER_COLOR.copy();
    public List<Item> ingredients = new ArrayList<>();
    public ItemStack output = ItemStack.EMPTY;

    boolean initialMarkDirtyCalled = false; // otherwise renderer won't show anything until something changes

    /* ------ DATA STORAGE & SYNC ------ */

    @Override
    public void markDirty()
    {
        if (world == null || world.isClient)
            return;
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos()))
            S2CWitchCauldronSyncPayload.send(player, getPos(), mode, waterAmount, ticksSinceModeSwitch, ingredients, currColor, goalColor);
        super.markDirty();
    }
    // used by client to update data from server
    public void setData(int mode, int waterAmount, int ticksSinceModeSwitch, List<Item> ingredients, Color currColor, Color goalColor)
    {
        this.mode = mode;
        this.waterAmount = waterAmount;
        this.ticksSinceModeSwitch = ticksSinceModeSwitch;
        this.ingredients = ingredients;
        this.currColor = currColor;
        this.goalColor = goalColor;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        super.writeNbt(nbt, registryLookup);

        nbt.putInt("mode", mode);
        nbt.putInt("waterAmount", waterAmount);
        nbt.putInt("ticksSinceModeSwitch", ticksSinceModeSwitch);
        nbt.put("currColor", currColor.asNbt());
        nbt.put("goalColor", goalColor.asNbt());

        nbt.putInt("ingredientsLength", ingredients.size());
        for (int i = 0; i < ingredients.size(); i++)
            nbt.putString("ingredient" + i, ingredients.get(i).toString());
    }
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        mode = nbt.getInt("mode");
        waterAmount = nbt.getInt("waterAmount");
        ticksSinceModeSwitch = nbt.getInt("ticksSinceModeSwitch");
        currColor = new Color(nbt.getCompound("currColor"));
        goalColor = new Color(nbt.getCompound("goalColor"));

        int ingredientsLength = nbt.getInt("ingredientsLength");
        ingredients.clear();
        for (int i = 0; i < ingredientsLength; i++)
            ingredients.add(Registries.ITEM.get(Identifier.of(nbt.getString("ingredient" + i))));

        markDirty();

        super.readNbt(nbt, registryLookup);
    }

    /* ------ FUNCTIONALITY ------ */

    public void tick(World world, BlockPos pos, BlockState state)
    {
        ticksSinceModeSwitch++;
        if (!initialMarkDirtyCalled)
        {
            markDirty();
            initialMarkDirtyCalled = true;
        }

        if (world.isClient)
            return;

        switch (mode)
        {
            case 0:
                currColor = WitchCauldronSettings.WATER_COLOR.copy();
                goalColor = WitchCauldronSettings.WATER_COLOR.copy();
                if (waterAmount > 0 && ticksSinceModeSwitch % 5 == 0)
                    attemptToPickUpIngredient();
                break;
            case 1:
                if (getHeatLevel(pos, world) >= waterAmount)
                {
                    mode = 2;
                    ticksSinceModeSwitch = 0;

                    ResultCalculator.ResultCalculatorOutput result = ResultCalculator.getResult(ingredients);
                    output = result.output;
                    goalColor = result.color;

                    markDirty();
                    break;
                }
                if (waterAmount > 0 && ticksSinceModeSwitch % 5 == 0)
                    attemptToPickUpIngredient();
                break;
            case 2:
                if (!currColor.equals(goalColor))
                {
                    currColor.shiftColorTowardsColor(goalColor, getColorShiftSpeed());
                    markDirty();
                }
                if (!ingredients.isEmpty() && ticksSinceModeSwitch >= WitchCauldronSettings.BREWING_MODE_DURATION_TICKS / 2)
                {
                    ingredients.clear();
                    markDirty();
                }
                if (ticksSinceModeSwitch >= WitchCauldronSettings.BREWING_MODE_DURATION_TICKS)
                {
                    mode = 3;
                    ticksSinceModeSwitch = 0;
                    markDirty();
                }
                break;
            case 3:
                if (waterAmount == 0)
                {
                    output = ItemStack.EMPTY;
                    currColor = WitchCauldronSettings.WATER_COLOR.copy();
                    goalColor = WitchCauldronSettings.WATER_COLOR.copy();
                    mode = 0;
                    ticksSinceModeSwitch = 0;
                    markDirty();
                }
                break;
        }
    }

    // returns whether an interaction occurred
    public boolean onUse(PlayerEntity player)
    {
        if (world == null || world.isClient())
            return false;

        boolean interactionOccurred = false;

        ItemStack heldStack = player.getStackInHand(Hand.MAIN_HAND);

        boolean holdingWaterBucket = heldStack.getItem() == Items.WATER_BUCKET;
        boolean holdingBucket = heldStack.getItem() == Items.BUCKET;
        boolean holdingGlassBottle = heldStack.getItem() == Items.POTION || heldStack.getItem() == Items.GLASS_BOTTLE
                || heldStack.getItem() == Items.LINGERING_POTION || heldStack.getItem() == Items.SPLASH_POTION;
        boolean glassBottleHasWater = false;
        if (holdingGlassBottle && heldStack.contains(DataComponentTypes.POTION_CONTENTS))
            glassBottleHasWater = heldStack.get(DataComponentTypes.POTION_CONTENTS).matches(Potions.WATER);

        GameMode gameMode = ((ServerPlayerEntity)player).interactionManager.getGameMode();

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
                        if (gameMode != GameMode.CREATIVE)
                        {
                            heldStack.decrement(1);
                            player.setStackInHand(Hand.MAIN_HAND, heldStack);
                            player.giveItemStack(Items.BUCKET.getDefaultStack());
                        }
                    }
                    else if (holdingGlassBottle && glassBottleHasWater)
                    {
                        waterAmount = waterAmount + 1; markDirty();
                        interactionOccurred = true;
                        if (gameMode != GameMode.CREATIVE)
                        {
                            heldStack.decrement(1);
                            player.setStackInHand(Hand.MAIN_HAND, heldStack);
                            player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
                        }
                    }
                }

                // getting water back out
                if (waterAmount > 0)
                {
                    if (holdingBucket)
                    {
                        interactionOccurred = true;
                        if (waterAmount == 3 && gameMode != GameMode.CREATIVE)
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
                        if (gameMode != GameMode.CREATIVE)
                        {
                            heldStack.decrement(1);
                            player.setStackInHand(Hand.MAIN_HAND, heldStack);
                            player.giveItemStack(PotionContentsComponent.createStack(Items.POTION, Potions.WATER));
                        }
                    }
                }
                break;

            case 1:
                if (player.getStackInHand(Hand.MAIN_HAND).isEmpty() && player.getStackInHand(Hand.OFF_HAND).isEmpty())
                {
                    if (!ingredients.isEmpty())
                    {
                        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ingredients.getFirst().getDefaultStack());
                        ingredients.removeFirst();
                        markDirty();
                        interactionOccurred = true;
                    }
                    if (ingredients.isEmpty())
                    {
                        mode = 0;
                        ticksSinceModeSwitch = 0;
                        markDirty();
                    }
                }
                break;

            case 3:
                if (heldStack.getItem() == Items.GLASS_BOTTLE)
                {
                    if (gameMode != GameMode.CREATIVE)
                    {
                        heldStack.decrement(1);
                        player.setStackInHand(Hand.MAIN_HAND, heldStack);
                    }
                    player.giveItemStack(output.copy());

                    waterAmount--;
                    markDirty();

                    interactionOccurred = true;
                }
                break;
        }

        return interactionOccurred;
    }

    private void attemptToPickUpIngredient()
    {
        Box areaToCheck = new Box(pos.getX(), pos.getY() + 0.2f, pos.getZ(), pos.getX() + 1, pos.getY() + 0.3f, pos.getZ() + 1);
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, areaToCheck, itemEntity -> true);

        if (!items.isEmpty() && mode != 1)
        {
            mode = 1;
            ticksSinceModeSwitch = 0;
            markDirty();
        }

        for (ItemEntity itemEntity : items)
        {
            if (ingredients.size() >= WitchCauldronSettings.MAX_INGREDIENTS)
                return;
            ItemStack itemStack = itemEntity.getStack();
            ingredients.add(itemStack.getItem());
            itemStack.decrement(1);
            markDirty();
        }
    }

    public void scatterIngredients(BlockPos pos, World world)
    {
        if (mode == 1 || mode == 2)
            for (Item ingredient : ingredients)
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ingredient.getDefaultStack());
        if (waterAmount == 3)
            world.setBlockState(pos, Blocks.WATER.getDefaultState());
    }

    private int getHeatLevel(BlockPos pos, World world)
    {
        // Level 3
        Box areaToCheck = new Box(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3, pos.getX() + 4, pos.getY() + 4, pos.getZ() + 4);
        if (!world.getEntitiesByClass(BlazeEntity.class, areaToCheck, entity -> true).isEmpty())
            return 3;

        // Level 2
        BlockState belowBlock = world.getBlockState(pos.down());
        if (belowBlock.isIn(ModBlockTags.HEATING_BLOCKS_LVL_2))
            return 2;

        // Level 1
        if (belowBlock.isIn(ModBlockTags.HEATING_BLOCKS_LVL_1))
            return 1;

        return 0;
    }

    public int getColorShiftSpeed()
    {
        int largestDiff = 1;
        largestDiff = Math.max(largestDiff, Math.abs(currColor.RED - goalColor.RED));
        largestDiff = Math.max(largestDiff, Math.abs(currColor.GREEN - goalColor.GREEN));
        largestDiff = Math.max(largestDiff, Math.abs(currColor.BLUE - goalColor.BLUE));
        largestDiff = Math.max(largestDiff, Math.abs(currColor.ALPHA - goalColor.ALPHA));

        return largestDiff / (WitchCauldronSettings.BREWING_MODE_DURATION_TICKS - ticksSinceModeSwitch);
    }
}
