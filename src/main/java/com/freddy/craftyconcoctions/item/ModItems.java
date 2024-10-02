package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems
{
    public static void register() {}

    public static final Item WITCH_POTION = registerItem("witch_potion", new Item(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static Item registerItem(String name, Item item)
    {
        return Registry.register(Registries.ITEM, Identifier.of(CraftyConcoctions.MOD_ID, name), item);
    }
}
