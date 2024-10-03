package com.freddy.craftyconcoctions.networking.payload;

import com.freddy.craftyconcoctions.networking.ModMessages;
import com.freddy.craftyconcoctions.util.Color;
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

public record S2CWitchCauldronSyncPayload(NbtCompound data) implements CustomPayload
{
    public static final CustomPayload.Id<S2CWitchCauldronSyncPayload> ID = new CustomPayload.Id<>(ModMessages.S2C_WITCH_CAULDRON_SYNC_ID);

    public static final PacketCodec<RegistryByteBuf, S2CWitchCauldronSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.NBT_COMPOUND, S2CWitchCauldronSyncPayload::data,
            S2CWitchCauldronSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return ID;
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, int mode, int waterAmount, int ticksSinceModeSwitch, List<Item> ingredients, Color currColor, Color goalColor)
    {
        NbtCompound nbt = new NbtCompound();

        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putInt("mode", mode);
        nbt.putInt("waterAmount", waterAmount);
        nbt.putInt("ticksSinceModeSwitch", ticksSinceModeSwitch);
        nbt.putInt("ingredientsLength", ingredients.size());
        for (int i = 0; i < ingredients.size(); i++)
            nbt.putString("item" + i, ingredients.get(i).toString());
        nbt.put("currColor", currColor.asNbt());
        nbt.put("goalColor", goalColor.asNbt());

        ServerPlayNetworking.send(player, new S2CWitchCauldronSyncPayload(nbt));
    }
}
