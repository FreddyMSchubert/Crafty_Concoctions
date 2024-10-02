package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntity;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ModMessagesClient
{
    public static void registerClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2CWitchCauldronSyncPayload.ID, (packet, context) -> {
            context.client().execute(() -> {
                BlockPos pos = packet.pos();
                BlockEntity blockEntity = context.client().world.getBlockEntity(pos);
                if (blockEntity instanceof WitchCauldronBlockEntity cauldronBlockEntity)
                    cauldronBlockEntity.setData(packet.mode(), packet.waterAmount(), packet.ticksSinceModeSwitch());
            });
        });
    }
}
