package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems
{
    public static void register() {}

    public static final Item WITCH_POTION = registerItem("witch_potion", new WitchPotionItem(new Item.Settings().maxCount(1)));
    public static final Item CLOVER_LEAVES_2 = registerItem("clover_leaves_2", new Item(new Item.Settings()));
    public static final Item CLOVER_LEAVES_3 = registerItem("clover_leaves_3", new Item(new Item.Settings()));
    public static final Item CLOVER_LEAVES_4 = registerItem("clover_leaves_4", new Item(new Item.Settings()));

    public static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, Identifier.of(CraftyConcoctions.MOD_ID, name), item);
    }
}
