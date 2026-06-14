package com.refluxstorage;

import com.refluxstorage.item.RefluxStorageItems;
import com.refluxstorage.network.RefluxStorageNetworking;
import com.refluxstorage.sound.RefluxStorageSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RefluxStorage.MOD_ID)
public class RefluxStorage {
    public static final String MOD_ID = "reflux_storage";
    private static final Logger LOGGER = LoggerFactory.getLogger(RefluxStorage.class);

    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> REFLUX_STORAGE_TAB =
        TABS.register("reflux_storage", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.reflux_storage"))
            .icon(() -> RefluxStorageItems.REFLUX_STORAGE.get().getDefaultInstance())
            .displayItems((parameters, output) -> RefluxStorageItems.addToCreativeTab(output))
            .build());

    public RefluxStorage() {
        LOGGER.info("Reflux Storage initializing...");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RefluxStorageItems.ITEMS.register(modEventBus);
        RefluxStorageSounds.SOUNDS.register(modEventBus);
        TABS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(RefluxStorageNetworking::register);
    }
}
