package com.github.saturnvolv.core.item.enchantment;

import com.github.saturnvolv.core.SaturnCore;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SaturnEnchantment implements Translatable {
    private final NamespacedKey key;
    private final EnchantmentSettings settings;
    private final Enchantment enchantment;
    public SaturnEnchantment(String key, EnchantmentSettings enchantmentSettings) {
        this.key = new NamespacedKey(SaturnCore.MOD_ID, key);
        this.settings = enchantmentSettings;
        this.enchantment = abstractEnchantmentBuilder();
    }

    public Enchantment enchantment() {
        return enchantment;
    }

    private Enchantment abstractEnchantmentBuilder() {
        Enchantment enchantment = new Enchantment() {
            @Override
            public @NotNull String getName() {
                return key.value();
            }

            @Override
            public int getMaxLevel() {
                return settings.maxLevel();
            }

            @Override
            public int getStartLevel() {
                return settings.maxLevel();
            }

            @Override
            public @NotNull EnchantmentTarget getItemTarget() {
                return settings.itemTarget();
            }

            @Override
            public boolean isTreasure() {
                return settings.treasure();
            }

            @Override
            public boolean isCursed() {
                return settings.cursed();
            }

            @Override
            public boolean conflictsWith( @NotNull Enchantment enchantment ) {
                SaturnEnchantment sEnchantment = fromEnchantment(enchantment);
                return false;
            }

            @Override
            public boolean canEnchantItem( @NotNull ItemStack stack ) {
                return false;
            }

            @Override
            public @NotNull Component displayName( int i ) {
                return Component.translatable(this.translationKey());
            }

            @Override
            public boolean isTradeable() {
                return settings.tradeable();
            }

            @Override
            public boolean isDiscoverable() {
                return settings.discoverable();
            }

            @Override
            public int getMinModifiedCost( int i ) {
                return 0;
            }

            @Override
            public int getMaxModifiedCost( int i ) {
                return 0;
            }

            @Override
            public @NotNull EnchantmentRarity getRarity() {
                return settings.rarity();
            }

            @Override
            public float getDamageIncrease( int i, @NotNull EntityCategory category ) {
                return 0;
            }

            @Override
            public @NotNull Set<EquipmentSlot> getActiveSlots() {
                return settings.activeSlots();
            }

            @Override
            public @NotNull String translationKey() {
                return String.format("enchantment.%1$s.%2$s", key.namespace(), key.value());
            }

            @Override
            public @NotNull NamespacedKey getKey() {
                return new NamespacedKey(SaturnCore.MOD_ID, key.value());
            }
        };
        return enchantment;
    }

    private static SaturnEnchantment fromEnchantment(Enchantment enchantment) {
        return new SaturnEnchantment(enchantment.key().value(),
                new EnchantmentSettings.Builder()
                        .maxLevel(enchantment.getMaxLevel())
                        .startLevel(enchantment.getStartLevel())
                        .itemTarget(enchantment.getItemTarget())
                        .treasure(enchantment.isTreasure())
                        .cursed(enchantment.isCursed())
                        .tradeable(enchantment.isTradeable())
                        .discoverable(enchantment.isDiscoverable())
                        .rarity(enchantment.getRarity())
                        .activeSlots(enchantment.getActiveSlots())
                        .build());
    }

    @Override
    public @NotNull String translationKey() {
        return String.format("enchantment.%1$s.%2$s", key.namespace(), key.value());
    }
}
