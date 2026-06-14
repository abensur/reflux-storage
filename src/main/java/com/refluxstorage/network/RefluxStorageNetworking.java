package com.refluxstorage.network;

import com.refluxstorage.compat.curios.RefluxStorageCuriosCompat;
import com.refluxstorage.item.RefluxStorageItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RefluxStorageNetworking {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(AdjustPowerPayload.TYPE, AdjustPowerPayload.STREAM_CODEC, RefluxStorageNetworking::handleAdjustPower);
        registrar.playToServer(UseRefluxStoragePayload.TYPE, UseRefluxStoragePayload.STREAM_CODEC, RefluxStorageNetworking::handleUseRefluxStorage);
    }

    private static void handleUseRefluxStorage(UseRefluxStoragePayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = findAccessibleRefluxStorage(player);
        if (!stack.isEmpty()) {
            RefluxStorageItem.useFromStack(stack, player.level(), player);
        }
    }

    private static void handleAdjustPower(AdjustPowerPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = findAccessibleRefluxStorage(player);
        if (stack.isEmpty()) {
            return;
        }

        RefluxStorageItem.adjustBurpPower(stack, Integer.signum(payload.delta()));
        int power = RefluxStorageItem.getBurpPower(stack);
        player.displayClientMessage(Component.translatable("message.reflux_storage.power", power), true);
    }

    private static ItemStack findHeldRefluxStorage(Player player) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHand.getItem() instanceof RefluxStorageItem) {
            return mainHand;
        }
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (offHand.getItem() instanceof RefluxStorageItem) {
            return offHand;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findAccessibleRefluxStorage(Player player) {
        ItemStack held = findHeldRefluxStorage(player);
        if (!held.isEmpty()) {
            return held;
        }

        if (ModList.get().isLoaded("curios")) {
            ItemStack equipped = RefluxStorageCuriosCompat.findEquippedRefluxStorage(player).orElse(ItemStack.EMPTY);
            if (!equipped.isEmpty()) {
                return equipped;
            }
        }

        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof RefluxStorageItem) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}
