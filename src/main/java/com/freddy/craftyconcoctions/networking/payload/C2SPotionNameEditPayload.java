package com.freddy.craftyconcoctions.networking.payload;

import com.freddy.craftyconcoctions.networking.ModMessagesClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record C2SPotionNameEditPayload(String name) implements CustomPayload
{
    public static final CustomPayload.Id<C2SPotionNameEditPayload> ID = new CustomPayload.Id<>(ModMessagesClient.C2S_POTION_NAME_EDIT_ID);

    public static final PacketCodec<RegistryByteBuf, C2SPotionNameEditPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, C2SPotionNameEditPayload::name,
            C2SPotionNameEditPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return null;
    }

    public static void send(String name)
    {
        ClientPlayNetworking.send(new C2SPotionNameEditPayload(name));
    }
}
