package com.refluxstorage.component;

import com.mojang.serialization.Codec;
import com.refluxstorage.RefluxStorage;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RefluxStorageComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RefluxStorage.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_MB =
        COMPONENTS.registerComponentType("stored_mb", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BURP_POWER =
        COMPONENTS.registerComponentType("burp_power", builder -> builder
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));
}
