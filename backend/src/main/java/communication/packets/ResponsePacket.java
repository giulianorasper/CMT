package communication.packets;

/**
 * An packet sent to clients as response to a {@link RequestPacket}.
 * This abstraction does not have a designated functionality yet. It has the sole purpose of increasing flexibility in case of code expansion.
 */
public abstract class ResponsePacket extends BasePacket {

    public ResponsePacket(PacketType packetType) {
        super(packetType);
    }

}
