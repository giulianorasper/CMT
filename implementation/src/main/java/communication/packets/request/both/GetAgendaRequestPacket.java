package communication.packets.request.both;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.both.GetAgendaResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet handles either an attendee or an admin requesting the current agenda and responds with a {@link GetAgendaResponsePacket}.
 */
public class GetAgendaRequestPacket extends AuthenticatedRequestPacket {

    public GetAgendaRequestPacket() {
        super(PacketType.GET_AGENDA_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
