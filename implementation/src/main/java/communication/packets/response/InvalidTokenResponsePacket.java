package communication.packets.response;

import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;

public class InvalidTokenResponsePacket extends ResponsePacket {

    public InvalidTokenResponsePacket() {
        super(PacketType.INVALID_TOKEN, RequestResult.InvalidToken);
    }
}
