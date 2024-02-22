package com.github.saturnvolv.core.item;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public enum ToolTier {
    AIR(0, 1),
    WOOD(0, 2),
    STONE(1, 4),
    IRON(2, 6),
    DIAMOND(3, 8),
    NETHERITE(3, 9),
    GOLD(0, 12);

    private final int miningLvl;
    private final int speedMultiplier;
    ToolTier(int miningLvl, int speedMultiplier) {
        this.miningLvl = miningLvl;
        this.speedMultiplier = speedMultiplier;
    }
    public int miningLvl() {
        return miningLvl;
    }
    public int speedMultiplier() {
        return speedMultiplier;
    }

    public static ToolTier fromItemStack( ItemStack itemStack ) {
        Material type = itemStack.getType();
        return switch (type) {
            case WOODEN_AXE, WOODEN_HOE, WOODEN_PICKAXE, WOODEN_SHOVEL, WOODEN_SWORD -> WOOD;
            case STONE_AXE, STONE_HOE, STONE_PICKAXE, STONE_SHOVEL, STONE_SWORD -> STONE;
            case IRON_AXE, IRON_HOE, IRON_PICKAXE, IRON_SHOVEL, IRON_SWORD -> IRON;
            case GOLDEN_AXE, GOLDEN_HOE, GOLDEN_PICKAXE, GOLDEN_SHOVEL, GOLDEN_SWORD -> GOLD;
            case DIAMOND_AXE, DIAMOND_HOE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_SWORD -> DIAMOND;
            case NETHERITE_AXE, NETHERITE_HOE, NETHERITE_PICKAXE, NETHERITE_SHOVEL, NETHERITE_SWORD -> NETHERITE;
            default -> AIR;
        };
    }
    public static int getMiningLevel( Block block ) {
        Material type = block.getType();
        if (Tag.NEEDS_STONE_TOOL.isTagged(type)) return 1;
        if (Tag.NEEDS_IRON_TOOL.isTagged(type)) return 2;
        if (Tag.NEEDS_DIAMOND_TOOL.isTagged(type)) return 3;
        return 0;
    }
}
