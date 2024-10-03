package com.freddy.craftyconcoctions.util;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDataComponentTypes
{
    public static final ComponentType<Integer> POTION_COLOR = ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build();

    public static void register()
    {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "potion_color"), POTION_COLOR);
    }
}
