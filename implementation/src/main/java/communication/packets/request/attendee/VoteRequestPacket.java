package communication.packets.request.attendee;

import communication.packets.Packet;
import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.both.FailureResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.OperationResponse;
import utils.Pair;
import voting.Voting;

/**
 * This packet handles an attendee requesting to submit their vote in an ongoing voting and on success responds with a general {@link communication.packets.ResponsePacket}.
 */
public class VoteRequestPacket extends AuthenticatedRequestPacket {

    private int voteID;
    private int optionID;

    /**
     *
     * @param voteID the ID of the voting the attendee wants to submit a vote for
     * @param optionID the ID of the choice the attendee wants to vote for
     */
    public VoteRequestPacket(int voteID, int optionID) {
        super(PacketType.VOTE_REQUEST);
        this.voteID = voteID;
        this.optionID = optionID;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, Voting> result = conference.getVoting(getToken(), voteID);
        if(isPermitted(webSocket, false, result.first())) {
            Voting voting = result.second();
            int userID = conference.getAttendeeData(getToken()).second().getID();
            Packet response;
            if(voting.addVote(optionID, userID)) {
                response = new ResponsePacket(PacketType.VOTE_RESPONSE, RequestResult.Valid);
            } else {
                response = new FailureResponsePacket();
            }
            response.send(webSocket);
        }
    }
}
