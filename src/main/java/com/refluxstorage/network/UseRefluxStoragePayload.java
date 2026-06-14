package com.refluxstorage.network;

import com.refluxstorage.RefluxStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UseRefluxStoragePayload() implements CustomPacketPayload {
    public static final UseRefluxStoragePayload INSTANCE = new UseRefluxStoragePayload();

    public static final CustomPacketPayload.Type<UseRefluxStoragePayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RefluxStorage.MOD_ID, "use_reflux_storage"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UseRefluxStoragePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
