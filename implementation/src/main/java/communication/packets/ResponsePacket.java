package communication.packets;

/**
 * A packet sent to clients as response to a {@link RequestPacket}.
 */
public abstract class ResponsePacket extends BasePacket {

    public ResponsePacket(PacketType packetType) {
        super(packetType);
    }

    private RequestResult result;

    public void setResult(RequestResult result) {
        this.result = result;
    }
}
