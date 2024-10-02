package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags
{
    // ingredients
    public static final TagKey<Item> INGREDIENTS =
            createTag("ingredients");
    public static final TagKey<Item> MODIFIERS =
            createTag("modifiers");
    public static final TagKey<Item> POTION_DEFINERS =
            createTag("potion_definers");

    private static TagKey<Item> createTag(String name)
    {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(CraftyConcoctions.MOD_ID, name));
    }
}
