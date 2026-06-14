package com.refluxstorage.item;

import com.refluxstorage.RefluxStorage;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RefluxStorageItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RefluxStorage.MOD_ID);

    public static final DeferredItem<RefluxStorageItem> REFLUX_STORAGE = ITEMS.register(
        "reflux_storage", () -> new RefluxStorageItem(new RefluxStorageItem.Properties()
            .stacksTo(1)
            .component(com.refluxstorage.component.RefluxStorageComponents.STORED_MB.get(), 0)
            .component(com.refluxstorage.component.RefluxStorageComponents.BURP_POWER.get(), RefluxStorageItem.DEFAULT_POWER)));

    public static void addToCreativeTab(CreativeModeTab.Output output) {
        output.accept(REFLUX_STORAGE.get());
    }
}
