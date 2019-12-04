package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;

public class ValidResponsePacket extends ResponsePacket {

    public ValidResponsePacket() {
        super(PacketType.VALID_RESPONSE, RequestResult.Valid);
    }
}
