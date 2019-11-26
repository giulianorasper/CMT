package communication.packets.response.attendee;

import communication.packets.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.request.attendee.VoteRequestPacket;

/**
 * This packet is sent as result of an {@link VoteRequestPacket}.
 */
public class VoteResponsePacket extends ResponsePacket {

    //TODO change this to an enum
    private String result;

    /**
     *
     * @param result the result of the vote submission (e.g. success, failure)
      */
    public VoteResponsePacket(String result) {
        super(PacketType.VOTE_RESPONSE);
        this.result = result;
    }
}
