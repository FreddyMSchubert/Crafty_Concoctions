package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.networking.payload.C2SPotionNameEditPayload;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ModMessages
{
    public static final Identifier S2C_WITCH_CAULDRON_SYNC_ID = Identifier.of(CraftyConcoctions.MOD_ID, "s2c_witch_cauldron_sync");

    public static void registerS2Csend()
    {
        PayloadTypeRegistry.playS2C().register(S2CWitchCauldronSyncPayload.ID, S2CWitchCauldronSyncPayload.CODEC);
    }

    public static void registerC2Sreceive()
    {
        ServerPlayNetworking.registerGlobalReceiver(C2SPotionNameEditPayload.ID, (payload, context) -> context.server().execute(() -> {
            ItemStack stack = context.player().getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof PotionItem)
                stack.set(ModDataComponentTypes.CUSTOM_POTION_NAME, payload.name());
        }));
    }
}
