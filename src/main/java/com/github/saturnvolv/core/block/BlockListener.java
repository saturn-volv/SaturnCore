package com.github.saturnvolv.core.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onNotePlay( NotePlayEvent e ) {
        e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockUpdate( BlockPhysicsEvent e ) {
        BlockData source = e.getChangedBlockData();
        if (source instanceof NoteBlock || source instanceof Tripwire) e.setCancelled(true);
    }
    @EventHandler
    public void onBlockPace( BlockPlaceEvent e ) {
        Block block = e.getBlock();
    }
}
