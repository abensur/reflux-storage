package com.refluxstorage;

import com.refluxstorage.component.RefluxStorageComponents;
import com.refluxstorage.item.RefluxStorageItems;
import com.refluxstorage.network.RefluxStorageNetworking;
import com.refluxstorage.sound.RefluxStorageSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RefluxStorage.MOD_ID)
public class RefluxStorage {
    public static final String MOD_ID = "reflux_storage";
    private static final Logger LOGGER = LoggerFactory.getLogger(RefluxStorage.class);

    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> REFLUX_STORAGE_TAB =
        TABS.register("reflux_storage", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.reflux_storage"))
            .icon(() -> RefluxStorageItems.REFLUX_STORAGE.get().getDefaultInstance())
            .displayItems((parameters, output) -> RefluxStorageItems.addToCreativeTab(output))
            .build());

    public RefluxStorage(ModContainer modContainer, IEventBus modEventBus) {
        LOGGER.info("Reflux Storage initializing...");

        RefluxStorageComponents.COMPONENTS.register(modEventBus);
        RefluxStorageItems.ITEMS.register(modEventBus);
        RefluxStorageSounds.SOUNDS.register(modEventBus);
        TABS.register(modEventBus);
        modEventBus.addListener(RefluxStorageNetworking::register);
    }
}
