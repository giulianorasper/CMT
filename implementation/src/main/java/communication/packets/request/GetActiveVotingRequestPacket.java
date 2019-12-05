package communication.packets.request;

import communication.packets.Packet;
import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import communication.packets.response.GetActiveVotingResponsePacket;
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
