package com.github.saturnvolv.core.item.enchantment;

import io.papermc.paper.enchantments.EnchantmentRarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EnchantmentSettings {
    private int maxLevel;
    private int startLevel;
    private EnchantmentTarget itemTarget;
    private boolean treasure;
    private boolean cursed;
    private List<SaturnEnchantment> conflicts = new ArrayList<>();
    private boolean tradeable;
    private boolean discoverable;
    private int minModifiedCost;
    private int maxModifiedCost;
    private EnchantmentRarity rarity;
    private int damageIncrease;
    private Set<EquipmentSlot> activeSlots;
    private EnchantmentSettings() {}

    public int maxLevel() {
        return maxLevel;
    }

    private void maxLevel( int maxLevel ) {
        this.maxLevel = maxLevel;
    }

    public int startLevel() {
        return startLevel;
    }

    private void startLevel( int startLevel ) {
        this.startLevel = startLevel;
    }

    public EnchantmentTarget itemTarget() {
        return itemTarget;
    }

    private void itemTarget( EnchantmentTarget itemTarget ) {
        this.itemTarget = itemTarget;
    }

    public boolean treasure() {
        return treasure;
    }

    private void treasure( boolean treasure ) {
        this.treasure = treasure;
    }

    public boolean cursed() {
        return cursed;
    }

    private void cursed( boolean cursed ) {
        this.cursed = cursed;
    }

    public List<SaturnEnchantment> conflicts() {
        return conflicts;
    }

    private void conflicts( List<SaturnEnchantment> conflicts ) {
        this.conflicts = conflicts;
    }

    public boolean tradeable() {
        return tradeable;
    }

    private void tradeable( boolean tradeable ) {
        this.tradeable = tradeable;
    }

    public boolean discoverable() {
        return discoverable;
    }

    private void discoverable( boolean discoverable ) {
        this.discoverable = discoverable;
    }

    public EnchantmentRarity rarity() {
        return rarity;
    }

    private void rarity( EnchantmentRarity rarity ) {
        this.rarity = rarity;
    }

    public Set<EquipmentSlot> activeSlots() {
        return activeSlots;
    }

    private void activeSlots( Set<EquipmentSlot> activeSlots ) {
        this.activeSlots = activeSlots;
    }

    public static class Builder {
        private EnchantmentSettings parent;
        public Builder() {
            this.parent = new EnchantmentSettings();
        }

        public Builder maxLevel(int maxLevel) {
            this.parent.maxLevel(maxLevel);
            return this;
        }
        public Builder startLevel(int startLevel) {
            this.parent.startLevel(startLevel);
            return this;
        }
        public Builder itemTarget(EnchantmentTarget itemTarget) {
            this.parent.itemTarget(itemTarget);
            return this;
        }
        public Builder treasure() {
            this.parent.treasure(true);
            return this;
        }
        public Builder treasure(boolean isTreasure) {
            this.parent.treasure(isTreasure);
            return this;
        }
        public Builder cursed() {
            this.parent.cursed(true);
            return this;
        }
        public Builder cursed(boolean isCursed) {
            this.parent.cursed(isCursed);
            return this;
        }
        public Builder conflictsWith(SaturnEnchantment... enchantments) {
            this.parent.conflicts(List.of(enchantments));
            return this;
        }
        public Builder conflictsWith(List<SaturnEnchantment> enchantments) {
            this.parent.conflicts(enchantments);
            return this;
        }
        public Builder tradeable() {
            this.parent.tradeable(true);
            return this;
        }
        public Builder tradeable(boolean isTradeable) {
            this.parent.tradeable(isTradeable);
            return this;
        }
        public Builder discoverable() {
            this.parent.discoverable(true);
            return this;
        }
        public Builder discoverable(boolean isDiscoverable) {
            this.parent.discoverable(isDiscoverable);
            return this;
        }
        public Builder rarity(EnchantmentRarity rarity) {
            this.parent.rarity(rarity);
            return this;
        }
        public Builder activeSlots(Set<EquipmentSlot> activeSlots) {
            this.parent.activeSlots(activeSlots);
            return this;
        }
        public Builder activeSlots(EquipmentSlot... activeSlots) {
            this.parent.activeSlots(Set.of(activeSlots));
            return this;
        }
        public EnchantmentSettings build() {
            return parent;
        }
    }
}
