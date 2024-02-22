package com.github.saturnvolv.core.item;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public enum ToolType {
    AXE,
    HOE,
    PICKAXE,
    SHOVEL,
    SWORD,
    HAND;

    public static ToolType fromItemStack(ItemStack item) {
        Material type = item.getType();
        if (Tag.ITEMS_AXES.isTagged(type)) return AXE;
        if (Tag.ITEMS_HOES.isTagged(type)) return HOE;
        if (Tag.ITEMS_PICKAXES.isTagged(type)) return PICKAXE;
        if (Tag.ITEMS_SHOVELS.isTagged(type)) return SHOVEL;
        if (Tag.ITEMS_SWORDS.isTagged(type)) return SWORD;
        return HAND;
    }
    public static ToolType fromBlock(Block block) {
        Material type = block.getType();
        if (Tag.MINEABLE_AXE.isTagged(type)) return AXE;
        if (Tag.MINEABLE_HOE.isTagged(type)) return HOE;
        if (Tag.MINEABLE_PICKAXE.isTagged(type)) return PICKAXE;
        if (Tag.MINEABLE_SHOVEL.isTagged(type)) return SHOVEL;
        if (Tag.SWORD_EFFICIENT.isTagged(type)) return SWORD;
        return HAND;
    }
}
