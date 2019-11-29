package communication.packets.response.attendee;

import communication.packets.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.request.attendee.GetActiveVotingRequestPacket;

/**
 * A response containing a boolean if there is an active voting. If there is an active voting it contains
 *  more detailed information on a voting. Sent as result of an {@link GetActiveVotingRequestPacket}.
 */
public class GetActiveVotingResponsePacket extends ResponsePacket {

    private boolean exists;
    private String title;
    private String[] options;

    /**
     * @param exists a boolean indicating if there currently is an active voting
     * @param title title of the active voting, null if there is none
     * @param options vote options (ordered) of the active voting, null if there is none
     */
    public GetActiveVotingResponsePacket(boolean exists, String title, String[] options) {
        super(PacketType.GET_ACTIVE_VOTING_RESPONSE);
        this.exists = exists;
        this.title = title;
        this.options = options;
    }
}
