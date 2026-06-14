package com.refluxstorage.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.refluxstorage.RefluxStorage;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public class RefluxStorageKeyMappings {
    public static final String CATEGORY = "key.categories." + RefluxStorage.MOD_ID;

    public static final KeyMapping USE_REFLUX_STORAGE = new KeyMapping(
        "key." + RefluxStorage.MOD_ID + ".use_reflux_storage",
        KeyConflictContext.IN_GAME,
        InputConstants.UNKNOWN,
        CATEGORY);

    public static final KeyMapping ADJUST_BURP_POWER = new KeyMapping(
        "key." + RefluxStorage.MOD_ID + ".adjust_burp_power",
        KeyConflictContext.IN_GAME,
        InputConstants.UNKNOWN,
        CATEGORY);

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(USE_REFLUX_STORAGE);
        event.register(ADJUST_BURP_POWER);
    }
}
