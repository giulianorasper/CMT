package communication.packets.response;

import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;

public class ValidResponsePacket extends ResponsePacket {

    public ValidResponsePacket() {
        super(PacketType.VALID_RESPONSE, RequestResult.Valid);
    }
}
