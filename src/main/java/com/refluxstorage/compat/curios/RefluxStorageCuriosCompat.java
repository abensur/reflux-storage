package com.refluxstorage.compat.curios;

import com.refluxstorage.item.RefluxStorageItem;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class RefluxStorageCuriosCompat {
    public static Optional<ItemStack> findEquippedRefluxStorage(Player player) {
        return CuriosApi.getCuriosInventory(player)
            .flatMap(RefluxStorageCuriosCompat::findInPreferredSlots);
    }

    private static Optional<ItemStack> findInPreferredSlots(ICuriosItemHandler handler) {
        return handler.findFirstCurio(RefluxStorageCuriosCompat::isRefluxStorage, "belt")
            .or(() -> handler.findFirstCurio(RefluxStorageCuriosCompat::isRefluxStorage, "charm"))
            .map(SlotResult::stack);
    }

    private static boolean isRefluxStorage(ItemStack stack) {
        return stack.getItem() instanceof RefluxStorageItem;
    }
}
