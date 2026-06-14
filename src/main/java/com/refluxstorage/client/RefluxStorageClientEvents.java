package com.refluxstorage.client;

import com.refluxstorage.RefluxStorage;
import com.refluxstorage.item.RefluxStorageItem;
import com.refluxstorage.network.AdjustPowerPayload;
import com.refluxstorage.network.UseRefluxStoragePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RefluxStorage.MOD_ID, value = Dist.CLIENT)
public class RefluxStorageClientEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        while (RefluxStorageKeyMappings.USE_REFLUX_STORAGE.consumeClick()) {
            PacketDistributor.sendToServer(UseRefluxStoragePayload.INSTANCE);
        }

        while (RefluxStorageKeyMappings.ADJUST_BURP_POWER.consumeClick()) {
            PacketDistributor.sendToServer(new AdjustPowerPayload(minecraft.player.isShiftKeyDown() ? -1 : 1));
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null || !player.isShiftKeyDown() || !isHoldingRefluxStorage(player)) {
            return;
        }

        double scrollDelta = event.getScrollDeltaY();
        if (scrollDelta == 0.0D) {
            return;
        }

        int delta = scrollDelta > 0 ? 1 : -1;
        PacketDistributor.sendToServer(new AdjustPowerPayload(delta));
        event.setCanceled(true);
    }

    private static boolean isHoldingRefluxStorage(Player player) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        return mainHand.getItem() instanceof RefluxStorageItem || offHand.getItem() instanceof RefluxStorageItem;
    }
}
