package com.github.saturnvolv.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.saturnvolv.core.block.BlockBreaker;
import com.github.saturnvolv.core.block.gameplay.SaturnBlock;
import com.github.saturnvolv.core.event.player.PlayerActionEvent;
import com.github.saturnvolv.core.runnable.PlayerTickRunnable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaturnCore extends JavaPlugin implements Listener {
    public static String MOD_ID = "saturn";
    private static SaturnCore plugin;
    private static ProtocolManager manager;
    @Override
    public void onEnable() {
        plugin = this;
        manager = ProtocolLibrary.getProtocolManager();

        // Moved this to be a lambda expression and possible to be set by the user to contain any extra math or can be overwritten entirely.
        SaturnBlock.BreakSpeedCalc.set(SaturnBlock.BreakSpeedCalc::vanilla);

        registerEventListener(get());
        registerEventListener(new BlockBreaker());
        PlayerActionEvent.initialize();

    }

    @EventHandler
    void onJoin( PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new PlayerTickRunnable(player).runTaskTimer(this, 0, 1);
    }
    public static void registerEventListener( Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, get());
    }
    @Override
    public void onDisable() {
    }
    public static SaturnCore get() {
        return plugin;
    }
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(MOD_ID, key);
    }
    public static ProtocolManager manager() {
        return manager;
    }
}
