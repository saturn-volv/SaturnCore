package com.github.saturnvolv.core.item.inventory;

import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.item.enchantment.EnchantmentSettings;
import com.github.saturnvolv.core.item.enchantment.SaturnEnchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SaturnItemStack implements ISaturnItem {
    private static final Map<Material, Integer> materialCounter = new HashMap<>();
    private final int ID;
    private final NamespacedKey key;
    private final Material type;
    private final boolean isCustomItem;

    public SaturnItemStack( String key ) {
        this.type = Material.AIR;
        this.key = SaturnCore.getKey(key);
        this.ID = materialCounter.getOrDefault(type(), 0);
        this.isCustomItem = true;
        materialCounter.put(type(), this.ID + 1);
    }
    public SaturnItemStack( String key, Material type ) {
        this.type = type;
        this.key = SaturnCore.getKey(key);
        this.ID = materialCounter.getOrDefault(type(), 0);
        this.isCustomItem = true;
        materialCounter.put(type(), this.ID + 1);
    }
    public SaturnItemStack( ItemStack itemStack ) {
        this.type = itemStack.getType();
        this.key = itemStack.getType().getKey();
        this.isCustomItem = SaturnItemStack.isCustomItem(itemStack);
        this.ID = itemStack.getItemMeta().getCustomModelData();
    }

    @Override
    public Material type() {
        if (!type.isAir()) return type;
        return Material.PAPER;
    }

    public boolean isCustomItem() {
        return isCustomItem;
    }

    @Override
    public ItemStack itemStack() {
        ItemStack item = new ItemStack(this.type());
        SaturnEnchantment glow = new SaturnEnchantment("glow", new EnchantmentSettings.Builder().build());
        if (this.glint()) item.addUnsafeEnchantment(glow.enchantment(), 1);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.translatable(this.translationKey()));
        meta.setCustomModelData(this.ID);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(this.key.namespace(), "id"), PersistentDataType.STRING, this.key.value());

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean glint() {
        return false;
    }

    @Override
    public @NotNull String translationKey() {
        return String.format("item.%1$s.%2$s", key.namespace(), key.value());
    }

    private static boolean isCustomItem(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(SaturnCore.getKey("id"));
    }
}
