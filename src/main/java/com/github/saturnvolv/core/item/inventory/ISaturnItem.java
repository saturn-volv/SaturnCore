package com.github.saturnvolv.core.item.inventory;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ISaturnItem extends Translatable {
    Material type();
    ItemStack itemStack();
    boolean glint();
}
