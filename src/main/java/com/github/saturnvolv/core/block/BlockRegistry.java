package com.github.saturnvolv.core.block;

import com.github.saturnvolv.core.block.gameplay.SaturnBlock;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    static final Map<BlockData, SaturnBlock> REGISTRY = new HashMap<>();

    public static @NotNull SaturnBlock registerBlock(SaturnBlock entry) {
        entry.register();
        REGISTRY.put(entry.blockData() , entry);
        return entry;
    }
    public static boolean contains(BlockData blockData) {
        return REGISTRY.containsKey(blockData);
    }
    public static SaturnBlock getBlock(BlockData data) {
        return REGISTRY.get(data);
    }
}
