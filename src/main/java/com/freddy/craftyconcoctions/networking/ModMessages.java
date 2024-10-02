package com.freddy.craftyconcoctions.networking;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.networking.payload.S2CWitchCauldronSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class ModMessages
{
    public static final Identifier S2C_WITCH_CAULDRON_SYNC_ID = Identifier.of(CraftyConcoctions.MOD_ID, "s2c_witch_cauldron_sync");

    public static void registerS2C()
    {
        PayloadTypeRegistry.playS2C().register(S2CWitchCauldronSyncPayload.ID, S2CWitchCauldronSyncPayload.CODEC);
    }
}
