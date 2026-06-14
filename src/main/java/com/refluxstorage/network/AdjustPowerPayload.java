package com.refluxstorage.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record AdjustPowerPayload(int delta) {
    public static void encode(AdjustPowerPayload payload, FriendlyByteBuf buffer) {
        buffer.writeVarInt(payload.delta());
    }

    public static AdjustPowerPayload decode(FriendlyByteBuf buffer) {
        return new AdjustPowerPayload(buffer.readVarInt());
    }

    public static void handle(AdjustPowerPayload payload, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> RefluxStorageNetworking.handleAdjustPower(payload, context));
        context.setPacketHandled(true);
    }
}
