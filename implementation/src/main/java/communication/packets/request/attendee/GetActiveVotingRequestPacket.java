package communication.packets.request.attendee;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import communication.packets.response.attendee.GetActiveVotingResponsePacket;
import utils.OperationResponse;
import utils.Pair;
import voting.Voting;

/**
 * This packet handles an attendee requesting the currently active voting and responds with an {@link GetActiveVotingResponsePacket}.
 */
public class GetActiveVotingRequestPacket extends AuthenticatedRequestPacket {

    public GetActiveVotingRequestPacket() {
        super(PacketType.GET_ACTIVE_VOTING_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, Voting> result = conference.getActiveVoting(getToken());
        if(isPermitted(webSocket, false, result.first())) {
            Voting voting = result.second();
        }
    }
}
