package com.freddy.craftyconcoctions.networking.payload;

import com.freddy.craftyconcoctions.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public record S2CWitchCauldronSyncPayload(BlockPos pos, int mode, int waterAmount, int ticksSinceModeSwitch, int ingredientsLength, NbtCompound ingredients) implements CustomPayload
{
    public static final CustomPayload.Id<S2CWitchCauldronSyncPayload> ID = new CustomPayload.Id<>(ModMessages.S2C_WITCH_CAULDRON_SYNC_ID);

    public static final PacketCodec<RegistryByteBuf, S2CWitchCauldronSyncPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, S2CWitchCauldronSyncPayload::pos,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::mode,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::waterAmount,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::ticksSinceModeSwitch,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::ingredientsLength,
            PacketCodecs.NBT_COMPOUND, S2CWitchCauldronSyncPayload::ingredients,
            S2CWitchCauldronSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return ID;
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, int mode, int waterAmount, int ticksSinceModeSwitch, List<Item> ingredients)
    {
        NbtCompound nbt = new NbtCompound();
        for (int i = 0; i < ingredients.size(); i++)
            nbt.putString("item" + i, ingredients.get(i).toString());
        ServerPlayNetworking.send(player, new S2CWitchCauldronSyncPayload(pos, mode, waterAmount, ticksSinceModeSwitch, ingredients.size(), nbt));
    }
}
