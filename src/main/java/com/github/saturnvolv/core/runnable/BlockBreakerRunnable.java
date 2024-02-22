package com.github.saturnvolv.core.runnable;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.saturnvolv.core.block.BlockBreakHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;

public class BlockBreakerRunnable extends BukkitRunnable {
    private final Player who;
    private long startTime;
    private long breakTime;
    boolean breakInstantly;
    boolean cancelled;
    BlockBreakHandler handler;
    public BlockBreakerRunnable(BlockBreakHandler handler, Player player) {
        super();
        this.handler = handler;
        this.who = player;
    }

    public boolean breakInstantly() {
        return breakInstantly;
    }

    public BlockBreakHandler handler() {
        return handler;
    }

    public int getStage() {
        if (this.startTime > System.currentTimeMillis()) return 99;
        if (this.breakInstantly()) return 1;
        return (int) (Math.floorDiv(getTime(), this.breakTime / 9) - 0.5);
    }
    public long getTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void run() {
        try {
            if (!handler.hasBlocks() || this.isCancelled()) return;
            int prevStage = 0;
            for (Block block : handler.blocks()) {
                if (prevStage != this.getStage()) nextStage(block);
                prevStage = this.getStage();
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    public void nextStage( Block block) {
        BlockPosition position = new BlockPosition(block.getLocation().toVector());
        BlockBreakHandler.setAnimationStage(position, this.getStage(),
                Integer.parseInt(String.valueOf(who.getEntityId()) + handler.blocks().indexOf(block))
        );
        if (this.getStage() >= 9 || this.breakInstantly()) this.breakBlock(block);
    }
    public void breakBlock( Block block ) {
        block.breakNaturally(true);
        if (this.handler.blocks().indexOf(block) == this.handler.blocks().size()-1) this.cancel();
    }

    @Override
    public void cancel() {
        super.cancel();
        this.cancelled = true;
    }

    public @NotNull BukkitTask runTaskTimerAsynchronously( @NotNull Plugin plugin, long delay, float timeToBreak ) throws IllegalArgumentException, IllegalStateException {
        this.breakInstantly = timeToBreak <= 0;
        this.startTime = System.currentTimeMillis() + (delay / 20 * 1000);
        this.breakTime = this.breakInstantly() ? 0 : (long) timeToBreak;
        long period = (long) timeToBreak * 20 / 9000;
        return super.runTaskTimer(plugin, delay, period);
    }
}
