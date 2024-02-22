package com.github.saturnvolv.core.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class AbstractPacket {
    protected PacketContainer handle;

    protected AbstractPacket(PacketContainer handle, PacketType type) {
        if (handle == null)
            throw new IllegalArgumentException("Packet handle cannot be null.");
        if (!Objects.equals(handle.getType(), type))
            throw  new IllegalArgumentException(handle.getHandle()
                    + " is not a packet of type " + type);

        this.handle = handle;
    }

    public PacketContainer getHandle() {
        return handle;
    }

    public void sendPacket(Player receiver) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, getHandle());
    }
    public void broadcastPacket() {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
    }
    public void receivePacket(Player sender) {
        ProtocolLibrary.getProtocolManager().receiveClientPacket(sender, getHandle());
    }
}
