package com.freddy.craftyconcoctions.util;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDataComponentTypes
{
    public static final ComponentType<Integer> POTION_COLOR = ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build();
    public static final ComponentType<NbtCompound> POTION_INGREDIENTS = ComponentType.<NbtCompound>builder().codec(NbtCompound.CODEC).packetCodec(PacketCodecs.UNLIMITED_NBT_COMPOUND).build();

    public static final ComponentType<Boolean> GOOD_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> BAD_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> MUNDANE_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> AWKWARD_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> THICK_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> DILUTED_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
    public static final ComponentType<Boolean> STRONG_POTION = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();

    public static void register()
    {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "potion_color"), POTION_COLOR);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "potion_ingredients"), POTION_INGREDIENTS);

        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "good_potion"), GOOD_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "bad_potion"), BAD_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "mundane_potion"), MUNDANE_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "awkward_potion"), AWKWARD_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "thick_potion"), THICK_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "diluted_potion"), DILUTED_POTION);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CraftyConcoctions.MOD_ID, "strong_potion"), STRONG_POTION);
    }
}
