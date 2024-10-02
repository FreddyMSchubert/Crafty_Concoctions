package com.freddy.craftyconcoctions.item;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups
{
    public static ItemGroup crafty_concoctions_group;

    public static void register()
    {
        crafty_concoctions_group = Registry.register(Registries.ITEM_GROUP,
                Identifier.of(CraftyConcoctions.MOD_ID, "crafty_concoctions_group"),
                FabricItemGroup.builder().displayName(Text.translatable("itemgroup.crafty_concoctions_group"))
                        .icon(() -> new ItemStack(ModBlocks.WITCH_CAULDRON)).entries((displayContext, entries) -> entries.add(new ItemStack(ModBlocks.WITCH_CAULDRON))).build());
    }
}
