package com.github.saturnvolv.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerTickEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    public PlayerTickEvent( @NotNull Player who ) {
        super(who);
    }
    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
