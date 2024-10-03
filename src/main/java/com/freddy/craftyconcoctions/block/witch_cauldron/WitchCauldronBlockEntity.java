package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.block.ModBlockTags;
import com.freddy.craftyconcoctions.item.ModItemTags;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import com.freddy.craftyconcoctions.util.Color;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.ItemEntity;
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
    public List<Item> ingredients = new ArrayList<>();
    public ItemStack output = ItemStack.EMPTY;
    public Color currColor = WitchCauldronSettings.WATER_COLOR;
    public Color goalColor = WitchCauldronSettings.WATER_COLOR;

    boolean initialMarkDirtyCalled = false; // otherwise renderer won't show anything until something changes

    private void switchModeTo(int newMode)
    {
        mode = newMode;
        ticksSinceModeSwitch = 0;

        switch (newMode)
        {
            case 0:
                ingredients.clear();
                output = ItemStack.EMPTY;
                goalColor = WitchCauldronSettings.WATER_COLOR;
                break;

            case 2:
                ResultCalculator.ResultCalculatorOutput result = ResultCalculator.getResult(ingredients);
                output = result.output;
                goalColor = result.color;
                markDirty();
                break;
        }

        markDirty();
    }

    /* ------ DATA STORAGE & SYNC ------ */

    @Override
    public void markDirty()
    {
        if (world == null || world.isClient)
            return;
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos()))
            S2CWitchCauldronSyncPayload.send(player, getPos(), mode, waterAmount, ticksSinceModeSwitch, ingredients);
        super.markDirty();
    }
    // used by client to update data from server
    public void setData(int mode, int waterAmount, int ticksSinceModeSwitch, List<Item> ingredients)
    {
        switchModeTo(mode);
        this.waterAmount = waterAmount;
        this.ticksSinceModeSwitch = ticksSinceModeSwitch;
        this.ingredients = ingredients;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        super.writeNbt(nbt, registryLookup);

        nbt.putInt("mode", mode);
        nbt.putInt("waterAmount", waterAmount);
        nbt.putInt("ticksSinceModeSwitch", ticksSinceModeSwitch);

        nbt.putInt("ingredientsLength", ingredients.size());
        for (int i = 0; i < ingredients.size(); i++)
            nbt.putString("ingredient" + i, ingredients.get(i).toString());
    }
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        switchModeTo(nbt.getInt("mode"));
        waterAmount = nbt.getInt("waterAmount");
        ticksSinceModeSwitch = nbt.getInt("ticksSinceModeSwitch");

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
            case 1:
                if (isHeated(pos, world))
                    switchModeTo(2);
            case 0:
                if (waterAmount > 0 && ticksSinceModeSwitch % 5 == 0)
                    attemptToPickUpIngredient();
                break;
            case 2:
                if (ticksSinceModeSwitch >= WitchCauldronSettings.BREWING_MODE_DURATION_TICKS / 2 && !ingredients.isEmpty())
                    ingredients.clear();
                if (ticksSinceModeSwitch >= WitchCauldronSettings.BREWING_MODE_DURATION_TICKS)
                    switchModeTo(3);
                break;
        }
    }

    // returns whether an interaction occurred
    public boolean onUse(PlayerEntity player)
    {
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
                    if (waterAmount == 0)
                        switchModeTo(0);

                    markDirty();

                    interactionOccurred = true;
                }
        }

        return interactionOccurred;
    }

    private void attemptToPickUpIngredient()
    {
        Box areaToCheck = new Box(pos.getX(), pos.getY() + 0.2f, pos.getZ(), pos.getX() + 1, pos.getY() + 0.3f, pos.getZ() + 1);
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, areaToCheck, itemEntity -> itemEntity.getStack().isIn(ModItemTags.INGREDIENTS));

        if (!items.isEmpty() && mode != 1)
            switchModeTo(1);

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

    public boolean isHeated(BlockPos pos, World world)
    {
        BlockPos belowPos = pos.down();
        BlockState belowState = world.getBlockState(belowPos);
        return belowState.isIn(ModBlockTags.HEATING_BLOCKS);
    }
}
