package com.freddy.craftyconcoctions.networking.payload;

import com.freddy.craftyconcoctions.networking.ModMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record S2CWitchCauldronSyncPayload(BlockPos pos, int mode, int waterAmount, int ticksSinceModeSwitch) implements CustomPayload
{
    public static final CustomPayload.Id<S2CWitchCauldronSyncPayload> ID = new CustomPayload.Id<>(ModMessages.S2C_WITCH_CAULDRON_SYNC_ID);

    public static final PacketCodec<RegistryByteBuf, S2CWitchCauldronSyncPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, S2CWitchCauldronSyncPayload::pos,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::mode,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::waterAmount,
            PacketCodecs.INTEGER, S2CWitchCauldronSyncPayload::ticksSinceModeSwitch,
            S2CWitchCauldronSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return ID;
    }
}
