package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.networking.ModMessagesClient;
import net.fabricmc.api.ClientModInitializer;

public class CraftyConcoctionsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModMessagesClient.registerClient();
    }
}
