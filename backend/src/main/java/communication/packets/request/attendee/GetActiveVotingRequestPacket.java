package communication.packets.request.attendee;

import communication.CommunicationFactory;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import org.java_websocket.WebSocket;
import communication.packets.response.attendee.GetActiveVotingResponsePacket;

/**
 * This packet handles an attendee requesting the currently active voting and responds with an {@link GetActiveVotingResponsePacket}.
 */
public class GetActiveVotingRequestPacket extends AuthenticatedRequestPacket {

    public GetActiveVotingRequestPacket() {
        super(PacketType.GET_ACTIVE_VOTING_REQUEST);
    }

    @Override
    public void handle(CommunicationFactory factory, WebSocket webSocket) {
        //TODO handle
    }
}
