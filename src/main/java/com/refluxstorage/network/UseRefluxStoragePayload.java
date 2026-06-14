package com.refluxstorage.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record UseRefluxStoragePayload() {
    public static final UseRefluxStoragePayload INSTANCE = new UseRefluxStoragePayload();

    public static void encode(UseRefluxStoragePayload payload, FriendlyByteBuf buffer) {
    }

    public static UseRefluxStoragePayload decode(FriendlyByteBuf buffer) {
        return INSTANCE;
    }

    public static void handle(UseRefluxStoragePayload payload, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> RefluxStorageNetworking.handleUseRefluxStorage(payload, context));
        context.setPacketHandled(true);
    }
}
