package com.refluxstorage.network;

import com.refluxstorage.RefluxStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AdjustPowerPayload(int delta) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AdjustPowerPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RefluxStorage.MOD_ID, "adjust_power"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AdjustPowerPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        AdjustPowerPayload::delta,
        AdjustPowerPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
