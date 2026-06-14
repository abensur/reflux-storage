package com.refluxstorage.item;

import com.refluxstorage.RefluxStorage;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RefluxStorageItems {
    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, RefluxStorage.MOD_ID);

    public static final RegistryObject<RefluxStorageItem> REFLUX_STORAGE = ITEMS.register(
        "reflux_storage", () -> new RefluxStorageItem(new RefluxStorageItem.Properties()
            .stacksTo(1)));

    public static void addToCreativeTab(CreativeModeTab.Output output) {
        output.accept(REFLUX_STORAGE.get());
    }
}
