package communication.packets.request.attendee;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.attendee.VoteResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet handles an attendee requesting to submit their vote in an ongoing voting and responds with an {@link VoteResponsePacket}.
 */
public class VoteRequestPacket extends AuthenticatedRequestPacket {

    private int voteID;
    private int choiceID;

    /**
     *
     * @param voteID the ID of the voting the attendee wants to submit a vote for
     * @param choiceID the ID of the choice the attendee wants to vote for
     */
    public VoteRequestPacket(int voteID, int choiceID) {
        super(PacketType.VOTE_REQUEST);
        this.voteID = voteID;
        this.choiceID = choiceID;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
