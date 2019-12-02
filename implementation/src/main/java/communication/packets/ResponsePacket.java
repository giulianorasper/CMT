package communication.packets;


/**
 * A packet sent to clients as response to a {@link RequestPacket}.
 * If this packet itself is sent (i.e. not as an extended version) it still represents the packet type of another subclass
 * but just includes minimal information on the result of the former request.
 */
public class ResponsePacket extends BasePacket {

    public ResponsePacket(PacketType packetType, RequestResult result) {
        super(packetType);
        this.result = result;
    }

    private RequestResult result;
}
