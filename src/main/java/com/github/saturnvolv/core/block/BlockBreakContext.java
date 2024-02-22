package com.github.saturnvolv.core.block;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;

public class BlockBreakContext {
    private final Player player;
    private final ItemStack item;
    private final int efficiencyLevel;
    private @Nullable final PotionEffect hasteEffect;
    private @Nullable final PotionEffect fatigueEffect;
    private final boolean inWater;
    private boolean hasAquaAffinity = false;
    private final boolean inAir;
    public BlockBreakContext(Player player, ItemStack itemStack) {
        this.player = player;
        this.item = itemStack;

        this.hasteEffect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);
        this.fatigueEffect = player.getPotionEffect(PotionEffectType.SLOW_DIGGING);

        this.efficiencyLevel = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);

        if (player.getInventory().getHelmet() != null)
            this.hasAquaAffinity = player.getInventory().getHelmet().containsEnchantment(Enchantment.WATER_WORKER);

        this.inWater = player.isInWater();
        this.inAir = player.getVelocity().getY() == 0 && !player.isFlying();
    }
    public Player player() {
        return player;
    }
    public ItemStack item() {
        return item;
    }
    public boolean hasEfficiency() {
        return efficiencyLevel > 0;
    }
    public boolean hasHaste() {
        return hasteEffect != null;
    }
    public boolean hasFatigue() {
        return fatigueEffect != null;
    }
    public int getHasteLevel() {
        if (!hasHaste()) return 0;
        return Math.max(hasteEffect.getAmplifier(), 0);
    }
    public int getFatigueLevel() {
        if (!hasFatigue()) return 0;
        return Math.max(fatigueEffect.getAmplifier(), 0);
    }
    public int efficiencyLevel() {
        return efficiencyLevel;
    }
    public boolean inWater() {
        return inWater;
    }
    public boolean hasAquaAffinity() {
        return hasAquaAffinity;
    }
    public boolean inAir() {
        return inAir;
    }
}

