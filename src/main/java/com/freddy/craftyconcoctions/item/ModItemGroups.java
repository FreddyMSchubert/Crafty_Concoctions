package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.block.ModBlocks;
import com.freddy.craftyconcoctions.block.witch_cauldron.ResultCalculator;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import oshi.util.tuples.Pair;

import java.util.List;

public class ModItemGroups
{
    public static ItemGroup crafty_concoctions_group;

    public static void register()
    {
        crafty_concoctions_group = Registry.register(Registries.ITEM_GROUP,
                Identifier.of(CraftyConcoctions.MOD_ID, "crafty_concoctions_group"),
                FabricItemGroup.builder().displayName(Text.translatable("itemgroup.crafty_concoctions_group"))
                        .icon(() -> new ItemStack(ModBlocks.WITCH_CAULDRON)).entries((displayContext, entries) -> {
                            int differentiationInt = 0;

                            entries.add(new ItemStack(ModBlocks.WITCH_CAULDRON));
                            entries.add(new ItemStack(ModItems.CLOVER_LEAVES_2));
                            entries.add(new ItemStack(ModItems.CLOVER_LEAVES_3));
                            entries.add(new ItemStack(ModItems.CLOVER_LEAVES_4));

                            for (int i = 0; i < 5 + 9; i++)
                                entries.add(createEmptyStack(differentiationInt++));

                            List<Pair<ResultCalculator.EffectData, ResultCalculator.EffectData>> pairs = ResultCalculator.getEffectPairs();
                            for (Pair<ResultCalculator.EffectData, ResultCalculator.EffectData> pair : pairs)
                            {
                                differentiationInt = addEntriesForPotionIngredient(pair.getA().getItems().getFirst(), entries, differentiationInt);
                                differentiationInt = addEntriesForPotionIngredient(pair.getB().getItems().getFirst(), entries, differentiationInt);
                            }

                        }).build());
    }

    private static ItemStack createEmptyStack(int differentiationInt)
    {
        ItemStack stack = new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
        stack.set(ModDataComponentTypes.CREATIVE_MENU_DIFFERENTIATION_INT, differentiationInt);
        return stack;
    }

    private static int addEntriesForPotionIngredient(Item potionItem, ItemGroup.Entries entries, int differentiationInt)
    {
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.REDSTONE)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.GLOWSTONE)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.GLOWSTONE, Items.REDSTONE)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.GUNPOWDER)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.GUNPOWDER, Items.GLOWSTONE, Items.REDSTONE)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.DRAGON_BREATH)).getOutput());
        entries.add(ResultCalculator.getResult(List.of(potionItem, Items.NETHER_WART, Items.DRAGON_BREATH, Items.GLOWSTONE, Items.REDSTONE)).getOutput());
        entries.add(createEmptyStack(differentiationInt++));
        return differentiationInt;
    }
}
