package com.github.saturnvolv.core.runnable;

import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.event.player.PlayerTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTickRunnable extends BukkitRunnable implements Listener {
    private final Player player;
    public PlayerTickRunnable(Player player) {
        this.player = player;
        SaturnCore.registerEventListener(this);
    }
    @Override
    public void run() {
        if (!this.isCancelled())
            Bukkit.getServer().getPluginManager().callEvent(new PlayerTickEvent(this.player));
    }
    @EventHandler
    void onPlayerLeave( PlayerQuitEvent event ) { if (event.getPlayer() == this.player) this.cancel(); }
}
