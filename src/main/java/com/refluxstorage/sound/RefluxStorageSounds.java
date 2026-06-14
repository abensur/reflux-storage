package com.refluxstorage.sound;

import com.refluxstorage.RefluxStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RefluxStorageSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(Registries.SOUND_EVENT, RefluxStorage.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BURP_WEAK = register("burp_weak");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURP_MEDIUM = register("burp_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURP_STRONG = register("burp_strong");
    public static final DeferredHolder<SoundEvent, SoundEvent> EMPTY = register("empty");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(RefluxStorage.MOD_ID, name)));
    }
}
