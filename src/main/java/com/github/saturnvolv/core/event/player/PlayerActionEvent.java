package com.github.saturnvolv.core.event.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.block.BlockBreakHandler;
import com.github.saturnvolv.core.wrappers.WrapperPlayClientBlockDig;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerActionEvent extends PlayerEvent implements Cancellable {
    static final ProtocolManager manager = SaturnCore.manager();
    private static final HandlerList handlers = new HandlerList();
    private final EnumWrappers.PlayerDigType status;
    private final BlockPosition position;
    private final EnumWrappers.Direction direction;
    private final WrapperPlayClientBlockDig wrappedPacket;

    public PlayerActionEvent( @NotNull Player who, WrapperPlayClientBlockDig packet ) {
        super(who, true);
        this.wrappedPacket = packet;
        this.status = wrappedPacket.getStatus();
        this.position = wrappedPacket.getLocation();
        this.direction = wrappedPacket.getDirection();
    }

    public static void initialize() {
        manager.addPacketListener(new PacketAdapter(SaturnCore.get(), ListenerPriority.LOW, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving( PacketEvent event) {
                SaturnCore.get().getServer().getPluginManager().callEvent(new PlayerActionEvent(event.getPlayer(), new WrapperPlayClientBlockDig(event.getPacket())));
            }
        });
    }

    public EnumWrappers.PlayerDigType getStatus() {
        return status;
    }

    /**
     * Checks if the event is block related.
     */
    public boolean hasBlock() {
        return List.of(EnumWrappers.PlayerDigType.START_DESTROY_BLOCK, EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK, EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK)
                .contains(this.status);
    }
    /**
     * Gets the location of the event's interaction.
     * Returns null if the event isn't block related.
     */
    public Location getBlockLocation() {
        if (hasBlock())
            return this.position.toLocation(this.player.getWorld());
        return null;
    }

    /**
     * Gets the block face being interacted with.
     * Returns null if the event isn't block related.
     */
    public BlockFace getBlockFace() {
        if (hasBlock())
            return BlockFace.valueOf(this.direction.name());
        return null;
    }
    /**
     * Gets the block of the event's interaction.
     * Returns null if the event isn't block related.
     */
    @Nullable
    public Block getBlock() {
        if (hasBlock())
            return getBlockLocation().getBlock();
        return null;
    }

    @Deprecated
    @NotNull
    public WrapperPlayClientBlockDig getWrappedPacket() {
        return this.wrappedPacket;
    }
    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }

    private boolean cancelled;
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
        if (b)
            BlockBreakHandler.setAnimationStage(this.wrappedPacket.getLocation(), 99, 0);
    }
}
