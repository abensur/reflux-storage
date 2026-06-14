package com.refluxstorage.network;

import com.refluxstorage.RefluxStorage;
import com.refluxstorage.compat.curios.RefluxStorageCuriosCompat;
import com.refluxstorage.item.RefluxStorageItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class RefluxStorageNetworking {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(RefluxStorage.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals);

    private static int packetId;

    public static void register() {
        CHANNEL.messageBuilder(AdjustPowerPayload.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(AdjustPowerPayload::encode)
            .decoder(AdjustPowerPayload::decode)
            .consumerMainThread(AdjustPowerPayload::handle)
            .add();
        CHANNEL.messageBuilder(UseRefluxStoragePayload.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(UseRefluxStoragePayload::encode)
            .decoder(UseRefluxStoragePayload::decode)
            .consumerMainThread(UseRefluxStoragePayload::handle)
            .add();
    }

    public static void sendToServer(Object payload) {
        CHANNEL.sendToServer(payload);
    }

    static void handleUseRefluxStorage(UseRefluxStoragePayload payload, NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        ItemStack stack = findAccessibleRefluxStorage(player);
        if (!stack.isEmpty()) {
            RefluxStorageItem.useFromStack(stack, player.level(), player);
        }
    }

    static void handleAdjustPower(AdjustPowerPayload payload, NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
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
