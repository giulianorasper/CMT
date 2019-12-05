package communication.packets.request.attendee;

import communication.packets.Packet;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.both.GetAgendaResponsePacket;
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
        if(isPermitted(conference, webSocket, false)) {
            Voting voting = conference.getActiveVoting();
            Packet response;
            if(voting != null) {
                response = new GetActiveVotingResponsePacket(voting);
            } else {
                response = new GetActiveVotingResponsePacket();
            }
            response.send(webSocket);
        }
    }
}
