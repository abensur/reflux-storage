package com.refluxstorage.item;

import com.refluxstorage.component.RefluxStorageComponents;
import com.refluxstorage.sound.RefluxStorageSounds;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RefluxStorageItem extends Item {
    public static final int CAPACITY_MB = 8000;
    public static final int CHARGE_PER_SECOND_MB = 25;
    public static final int COST_PER_POWER_MB = 100;
    public static final int MIN_POWER = 1;
    public static final int MAX_POWER = 20;
    public static final int DEFAULT_POWER = 3;
    public static final int COOLDOWN_TICKS = 12;

    private static final double BASE_VERTICAL_IMPULSE = 0.52D;
    private static final double POWER_VERTICAL_SCALE = 0.08D;

    public RefluxStorageItem(Properties properties) {
        super(properties);
    }

    public static int getStoredMb(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(RefluxStorageComponents.STORED_MB.get(), 0), 0, CAPACITY_MB);
    }

    public static void setStoredMb(ItemStack stack, int storedMb) {
        stack.set(RefluxStorageComponents.STORED_MB.get(), Mth.clamp(storedMb, 0, CAPACITY_MB));
    }

    public static int getBurpPower(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(RefluxStorageComponents.BURP_POWER.get(), DEFAULT_POWER), MIN_POWER, MAX_POWER);
    }

    public static void setBurpPower(ItemStack stack, int power) {
        stack.set(RefluxStorageComponents.BURP_POWER.get(), Mth.clamp(power, MIN_POWER, MAX_POWER));
    }

    public static boolean adjustBurpPower(ItemStack stack, int delta) {
        int current = getBurpPower(stack);
        int next = Mth.clamp(current + delta, MIN_POWER, MAX_POWER);
        setBurpPower(stack, next);
        return next != current;
    }

    public static boolean charge(ItemStack stack, int amountMb) {
        int stored = getStoredMb(stack);
        if (stored >= CAPACITY_MB) {
            return false;
        }
        setStoredMb(stack, stored + amountMb);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        return useFromStack(stack, level, player)
            ? InteractionResultHolder.consume(stack)
            : InteractionResultHolder.fail(stack);
    }

    public static boolean useFromStack(ItemStack stack, Level level, Player player) {
        if (!(stack.getItem() instanceof RefluxStorageItem refluxStorageItem)) {
            return false;
        }

        if (player.getCooldowns().isOnCooldown(refluxStorageItem)) {
            return false;
        }

        int power = getBurpPower(stack);
        int cost = power * COST_PER_POWER_MB;
        int stored = getStoredMb(stack);
        if (stored < cost) {
            player.displayClientMessage(Component.translatable("message.reflux_storage.not_enough", stored, cost), true);
            level.playSound(null, player, RefluxStorageSounds.EMPTY.get(), SoundSource.PLAYERS, 0.45F, 1.35F);
            player.getCooldowns().addCooldown(refluxStorageItem, COOLDOWN_TICKS);
            return false;
        }

        setStoredMb(stack, stored - cost);
        launch(player, power);
        level.playSound(null, player, getBurpSound(power), SoundSource.PLAYERS, getBurpVolume(power), getBurpPitch(player, power));
        player.getCooldowns().addCooldown(refluxStorageItem, COOLDOWN_TICKS);
        return true;
    }

    private static void launch(Player player, int power) {
        double yImpulse = BASE_VERTICAL_IMPULSE + power * POWER_VERTICAL_SCALE;
        Vec3 velocity = player.getDeltaMovement();
        player.setDeltaMovement(velocity.x, yImpulse, velocity.z);
        player.hasImpulse = true;
        player.hurtMarked = true;
    }

    private static SoundEvent getBurpSound(int power) {
        if (power <= MIN_POWER) {
            return RefluxStorageSounds.BURP_WEAK.get();
        }
        if (power >= MAX_POWER) {
            return RefluxStorageSounds.BURP_STRONG.get();
        }
        return RefluxStorageSounds.BURP_MEDIUM.get();
    }

    private static float getBurpVolume(int power) {
        if (power <= MIN_POWER) {
            return 0.65F;
        }
        if (power >= MAX_POWER) {
            return 1.35F;
        }

        float normalized = (power - 2.0F) / (MAX_POWER - 3.0F);
        return Mth.clamp(0.75F + normalized * 0.45F, 0.75F, 1.2F);
    }

    private static float getBurpPitch(Player player, int power) {
        if (power <= MIN_POWER) {
            return 1.15F + player.getRandom().nextFloat() * 0.12F;
        }
        if (power >= MAX_POWER) {
            return 0.78F + player.getRandom().nextFloat() * 0.08F;
        }
        return 0.95F + player.getRandom().nextFloat() * 0.16F;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getStoredMb(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getStoredMb(stack) * 13.0F / CAPACITY_MB);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fill = (float) getStoredMb(stack) / CAPACITY_MB;
        return Mth.hsvToRgb(0.48F + fill * 0.08F, 0.85F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.reflux_storage.stored", getStoredMb(stack), CAPACITY_MB)
            .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("tooltip.reflux_storage.power", getBurpPower(stack), getBurpPower(stack) * COST_PER_POWER_MB)
            .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.reflux_storage.controls")
            .withStyle(ChatFormatting.GRAY));
    }
}
