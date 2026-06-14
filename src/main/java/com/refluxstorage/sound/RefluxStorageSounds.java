package com.refluxstorage.sound;

import com.refluxstorage.RefluxStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RefluxStorageSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RefluxStorage.MOD_ID);

    public static final RegistryObject<SoundEvent> BURP_WEAK = register("burp_weak");
    public static final RegistryObject<SoundEvent> BURP_MEDIUM = register("burp_medium");
    public static final RegistryObject<SoundEvent> BURP_STRONG = register("burp_strong");
    public static final RegistryObject<SoundEvent> EMPTY = register("empty");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
            new ResourceLocation(RefluxStorage.MOD_ID, name)));
    }
}
