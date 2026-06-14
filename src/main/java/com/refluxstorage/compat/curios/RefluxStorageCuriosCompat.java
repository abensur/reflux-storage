package com.refluxstorage.compat.curios;

import com.refluxstorage.item.RefluxStorageItem;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class RefluxStorageCuriosCompat {
    public static Optional<ItemStack> findEquippedRefluxStorage(Player player) {
        return CuriosApi.getCuriosInventory(player)
            .resolve()
            .flatMap(RefluxStorageCuriosCompat::findInPreferredSlots);
    }

    private static Optional<ItemStack> findInPreferredSlots(ICuriosItemHandler handler) {
        return handler.findCurios("belt").stream()
            .map(result -> result.stack())
            .filter(RefluxStorageCuriosCompat::isRefluxStorage)
            .findFirst()
            .or(() -> handler.findCurios("charm").stream()
                .map(result -> result.stack())
                .filter(RefluxStorageCuriosCompat::isRefluxStorage)
                .findFirst());
    }

    private static boolean isRefluxStorage(ItemStack stack) {
        return stack.getItem() instanceof RefluxStorageItem;
    }
}
