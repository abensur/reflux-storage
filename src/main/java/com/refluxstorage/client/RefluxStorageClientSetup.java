package com.refluxstorage.client;

import com.refluxstorage.RefluxStorage;
import com.refluxstorage.item.RefluxStorageItem;
import com.refluxstorage.item.RefluxStorageItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = RefluxStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RefluxStorageClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
            RefluxStorageItems.REFLUX_STORAGE.get(),
            ResourceLocation.fromNamespaceAndPath(RefluxStorage.MOD_ID, "stored"),
            (stack, level, entity, seed) -> (float) RefluxStorageItem.getStoredMb(stack) / RefluxStorageItem.CAPACITY_MB));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        RefluxStorageKeyMappings.register(event);
    }
}
