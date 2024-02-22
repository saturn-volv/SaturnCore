package com.github.saturnvolv.core.block;

import com.comphenix.protocol.ProtocolManager;
import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.event.player.PlayerActionEvent;
import com.github.saturnvolv.core.event.player.PlayerTickEvent;
import com.github.saturnvolv.core.runnable.BlockBreakerRunnable;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class BlockBreaker implements Listener {
    public static final Map<UUID, Integer> delayBreak = new HashMap<>();
    public static final Map<UUID, BukkitTask> activeBreakers = new HashMap<>();
    public static final Map<UUID, BlockBreakHandler> activeHandlers = new HashMap<>();
    ProtocolManager manager = SaturnCore.manager();

    @EventHandler
    public void onPlayerAction( PlayerActionEvent event ) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        UUID uuid = player.getUniqueId();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        if (!event.hasBlock() || block.getType().isAir()) return;

        BlockBreakHandler handler = activeHandlers.getOrDefault(
                uuid, new BlockBreakHandler(player, new ArrayList<>(List.of(block)))
        );
        activeHandlers.put(uuid, handler);
        switch (event.getStatus()) {
            case START_DESTROY_BLOCK -> {
                BlockBreakerRunnable breakerRunnable = new BlockBreakerRunnable(handler, player) {
                    @Override
                    public void breakBlock(Block b) {
                        super.breakBlock(b);
                        if (!this.breakInstantly()) delayBreak.put(uuid, 6);
                    }
                    @Override
                    public void cancel() {
                        super.cancel();
                        this.handler().cancelBlockStageAnimation(player);
                        activeHandlers.remove(uuid);
                    }
                };
                activeBreakers.put(uuid, startRunnable(breakerRunnable, new BlockBreakContext(player, item)));
            }
            case ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK -> {
                activeBreakers.get(uuid).cancel();
                activeHandlers.remove(uuid);
                handler.cancelBlockStageAnimation(player);
            }
        }
    }
    public static BukkitTask startRunnable(BlockBreakerRunnable runnable, BlockBreakContext context) {
        float breakSpeed = runnable.handler().getBreakSpeed();
        return runnable.runTaskTimerAsynchronously(
                SaturnCore.get(),
                delayBreak.getOrDefault(context.player().getUniqueId(), 0),
                breakSpeed
        );
    }
    @EventHandler
    public void onPlayerTick( PlayerTickEvent event ) {
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, -1, -1, true, false, false));

        UUID uuid = player.getUniqueId();
        if (delayBreak.containsKey(uuid)) {
            delayBreak.put(uuid, delayBreak.get(uuid) - 1);
            if (delayBreak.get(uuid) <= 0) delayBreak.remove(uuid);
        }
    }
}