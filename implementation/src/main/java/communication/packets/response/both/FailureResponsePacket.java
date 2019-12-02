package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;

public class FailureResponsePacket extends ResponsePacket {

    public FailureResponsePacket() {
        super(PacketType.FAILURE, RequestResult.Failure);
    }
}
