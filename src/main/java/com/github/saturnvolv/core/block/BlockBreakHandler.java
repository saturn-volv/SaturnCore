package com.github.saturnvolv.core.block;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.saturnvolv.core.SaturnCore;
import com.github.saturnvolv.core.block.gameplay.SaturnBlock;
import com.github.saturnvolv.core.wrappers.WrapperPlayClientBlockDig;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockBreakHandler {
    private final ArrayList<Block> blocks;
    private final BlockBreakContext context;

    @Deprecated
    public BlockBreakHandler( Player player, WrapperPlayClientBlockDig container ) {
        ItemStack activeItem = player.getInventory().getItemInMainHand();
        this.context = new BlockBreakContext(player, activeItem);
        this.blocks = new ArrayList<>(List.of(container.getBlock(player.getWorld())));
    }
    public BlockBreakHandler( Player player, ArrayList<Block> blocks ) {
        ItemStack activeItem = player.getInventory().getItemInMainHand();
        this.context = new BlockBreakContext(player, activeItem);
        this.blocks = blocks;
    }
    public List<Location> getLocations() {
        return blocks.stream().map(Block::getLocation).collect(Collectors.toList());
    }
    public boolean hasBlocks() {
        return !this.blocks.isEmpty();
    }
    public ArrayList<Block> blocks() {
        return blocks;
    }
    public BlockBreakContext context() {
        return context;
    }
    public void cancelBlockStageAnimation( Player entity ) {
        while (this.hasBlocks()) {
            Block block = blocks().remove(0);
            BlockBreakHandler.setAnimationStage(
                    new BlockPosition(block.getLocation().toVector()),
                    99,
                    Integer.parseInt(String.valueOf(entity.getEntityId()) + blocks().size())
            );
        }
    }
    public float getBreakSpeed() {
        float breakSpeed = (float) blocks().stream().mapToDouble((b) -> getBreakSpeed(b, context())).sum();
        if (this.blocks().size() > 1) breakSpeed /= this.blocks().size() * 0.25f;
        return breakSpeed;
    }

    public static void setAnimationStage(BlockPosition position, int stage, int entityID) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        container.getBlockPositionModifier().write(0, position);
        container.getIntegers().write(0, entityID);
        container.getIntegers().write(1, stage);
        SaturnCore.manager().broadcastServerPacket(container);
    }
    public static float blockHardness(Block block) {
        if (SaturnBlock.exists(block)) return SaturnBlock.get(block).hardness();
        return block.getType().getHardness();
    }
    public static float getBreakSpeed(Block block, BlockBreakContext context) {
        if (SaturnBlock.exists(block)) return SaturnBlock.get(block).getBreakSpeed( context );
        return SaturnBlock.getBreakSpeed(blockHardness(block), block, context);
    }
}
