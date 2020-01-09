package communication.packets.request;

import communication.enums.RequestResult;
import communication.packets.Packet;
import communication.enums.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.FailureResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.Voting;

/**
 * This packet can be used by an attendee to submit their vote for an ongoing voting.
 * Responds with a {@link communication.packets.ResponsePacket}.
 */
public class AddVoteRequestPacket extends AuthenticatedRequestPacket {

    private int voteID;
    private int optionID;

    /**
     *
     * @param voteID the ID of the voting the attendee wants to submit a vote for
     * @param optionID the ID of the choice the attendee wants to vote for
     */
    public AddVoteRequestPacket(int voteID, int optionID) {
        super(PacketType.ADD_VOTE_REQUEST);
        this.voteID = voteID;
        this.optionID = optionID;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Voting voting = conference.getVoting(voteID);
            int userID = conference.tokenToID(getToken());
            Packet response;
            if(voting.addVote(optionID, userID)) {
                response = new ResponsePacket(PacketType.ADD_VOTE_RESPONSE, RequestResult.Valid);
            } else {
                response = new FailureResponsePacket();
            }
            response.send(webSocket);
        }
    }
}
