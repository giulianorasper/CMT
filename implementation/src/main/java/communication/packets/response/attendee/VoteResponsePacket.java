package communication.packets.response.attendee;

import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;
import communication.packets.request.attendee.VoteRequestPacket;

/**
 * This packet is sent as result of an {@link VoteRequestPacket}.
 */
public class VoteResponsePacket extends ResponsePacket {

    //TODO Any further information?

    public VoteResponsePacket(RequestResult result) {
        super(PacketType.VOTE_RESPONSE, result);
    }
}
