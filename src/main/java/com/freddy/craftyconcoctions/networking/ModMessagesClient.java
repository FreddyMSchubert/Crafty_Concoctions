package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntity;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ModMessagesClient
{
    public static void registerClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2CWitchCauldronSyncPayload.ID, (packet, context) -> {
            context.client().execute(() -> {
                BlockPos pos = packet.pos();
                BlockEntity blockEntity = context.client().world.getBlockEntity(pos);
                if (blockEntity instanceof WitchCauldronBlockEntity cauldronBlockEntity)
                {
                    NbtCompound nbt = packet.ingredients();
                    List<Item> ingredients = new ArrayList<>();
                    for (int i = 0; i < packet.ingredientsLength(); i++)
                        ingredients.add(Registries.ITEM.get(Identifier.of(nbt.getString("item" + i))));
                    cauldronBlockEntity.setData(packet.mode(), packet.waterAmount(), packet.ticksSinceModeSwitch(), ingredients);
                }
            });
        });
    }
}
