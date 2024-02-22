package com.github.saturnvolv.core.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WrapperPlayClientBlockDig extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;
    public WrapperPlayClientBlockDig() {
        super(new PacketContainer(TYPE), TYPE);
    }
    public WrapperPlayClientBlockDig(PacketContainer packet) {
        super(packet, TYPE);
    }

    public BlockPosition getLocation() {
        return handle.getBlockPositionModifier().read(0);
    }
    public void setLocation(BlockPosition value) {
        handle.getBlockPositionModifier().write(0, value);
    }
    public Direction getDirection() {
        return handle.getDirections().read(0);
    }
    public void setDirection(Direction value) {
        handle.getDirections().write(0, value);
    }
    public PlayerDigType getStatus() {
        return handle.getPlayerDigTypes().read(0);
    }
    public void setStatus(PlayerDigType value) {
        handle.getPlayerDigTypes().write(0, value);
    }

    public Block getBlock(World world) {
        return world.getBlockAt(this.getLocation().toLocation(world));
    }
}
