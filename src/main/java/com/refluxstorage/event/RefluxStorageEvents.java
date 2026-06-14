package com.refluxstorage.event;

import com.refluxstorage.RefluxStorage;
import com.refluxstorage.compat.curios.RefluxStorageCuriosCompat;
import com.refluxstorage.item.RefluxStorageItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = RefluxStorage.MOD_ID)
public class RefluxStorageEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;
        if (player.level().isClientSide() || player.tickCount % 20 != 0) {
            return;
        }

        findChargeTarget(player).ifPresent(stack -> RefluxStorageItem.charge(stack, RefluxStorageItem.CHARGE_PER_SECOND_MB));
    }

    private static java.util.Optional<ItemStack> findChargeTarget(Player player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof RefluxStorageItem && RefluxStorageItem.getStoredMb(stack) < RefluxStorageItem.CAPACITY_MB) {
                return java.util.Optional.of(stack);
            }
        }

        if (ModList.get().isLoaded("curios")) {
            return RefluxStorageCuriosCompat.findEquippedRefluxStorage(player)
                .filter(stack -> RefluxStorageItem.getStoredMb(stack) < RefluxStorageItem.CAPACITY_MB);
        }

        return java.util.Optional.empty();
    }
}
