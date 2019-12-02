package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;

public class InvalidTokenResponsePacket extends ResponsePacket {

    public InvalidTokenResponsePacket() {
        super(PacketType.INVALID_TOKEN, RequestResult.InvalidToken);
    }
}
