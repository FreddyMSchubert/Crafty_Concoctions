package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntity;
import com.freddy.craftyconcoctions.networking.payload.C2SPotionNameEditPayload;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import com.freddy.craftyconcoctions.util.Color;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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
    public static final Identifier C2S_POTION_NAME_EDIT_ID = Identifier.of(CraftyConcoctions.MOD_ID, "c2s_potion_name_edit");

    public static void registerC2Ssend()
    {
        PayloadTypeRegistry.playC2S().register(C2SPotionNameEditPayload.ID, C2SPotionNameEditPayload.CODEC);
    }

    public static void registerS2Creceive()
    {
        ClientPlayNetworking.registerGlobalReceiver(S2CWitchCauldronSyncPayload.ID, (packet, context) -> context.client().execute(() -> {
            NbtCompound data = packet.data();

            BlockPos pos = new BlockPos(data.getInt("x"), data.getInt("y"), data.getInt("z"));
            int mode = data.getInt("mode");
            int waterAmount = data.getInt("waterAmount");
            int ticksSinceModeSwitch = data.getInt("ticksSinceModeSwitch");
            int ingredientsLength = data.getInt("ingredientsLength");
            List<Item> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsLength; i++)
                ingredients.add(Registries.ITEM.get(Identifier.of(data.getString("item" + i))));
            Color currColor = new Color(data.getCompound("currColor"));
            Color goalColor = new Color(data.getCompound("goalColor"));

            BlockEntity blockEntity = context.client().world.getBlockEntity(pos);
            if (blockEntity instanceof WitchCauldronBlockEntity cauldronBlockEntity)
            {
                cauldronBlockEntity.setData(mode, waterAmount, ticksSinceModeSwitch, ingredients, currColor, goalColor);
            }
        }));
    }
}
