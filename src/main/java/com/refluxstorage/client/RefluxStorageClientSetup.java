package com.refluxstorage.client;

import com.refluxstorage.RefluxStorage;
import com.refluxstorage.item.RefluxStorageItem;
import com.refluxstorage.item.RefluxStorageItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RefluxStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RefluxStorageClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
            RefluxStorageItems.REFLUX_STORAGE.get(),
            new ResourceLocation(RefluxStorage.MOD_ID, "stored"),
            (stack, level, entity, seed) -> (float) RefluxStorageItem.getStoredMb(stack) / RefluxStorageItem.CAPACITY_MB));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        RefluxStorageKeyMappings.register(event);
    }
}
