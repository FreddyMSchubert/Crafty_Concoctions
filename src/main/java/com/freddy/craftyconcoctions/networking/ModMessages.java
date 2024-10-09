package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlockEntity;
import com.freddy.craftyconcoctions.networking.payload.C2SPotionNameEditPayload;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import com.freddy.craftyconcoctions.util.Color;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ModMessages
{
    public static final Identifier S2C_WITCH_CAULDRON_SYNC_ID = Identifier.of(CraftyConcoctions.MOD_ID, "s2c_witch_cauldron_sync");
    public static final Identifier C2S_POTION_NAME_EDIT_ID = Identifier.of(CraftyConcoctions.MOD_ID, "c2s_potion_name_edit");

    // Payload Types
    public static void registerClientPayloadTypes()
    {
        PayloadTypeRegistry.playS2C().register(S2CWitchCauldronSyncPayload.ID, S2CWitchCauldronSyncPayload.CODEC);
    }
    public static void registerServerPayloadTypes()
    {
        PayloadTypeRegistry.playC2S().register(C2SPotionNameEditPayload.ID, C2SPotionNameEditPayload.CODEC);
    }

    // Handlers
    public static void registerC2Sreceive()
    {
        ServerPlayNetworking.registerGlobalReceiver(C2SPotionNameEditPayload.ID, (payload, context) -> context.server().execute(() -> {
            ItemStack stack = context.player().getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof PotionItem)
                stack.set(ModDataComponentTypes.CUSTOM_POTION_NAME, payload.name());
        }));
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
