package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.GetAgendaResponsePacket;
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
        if(isPermitted(conference, webSocket, false)) {
            new GetAgendaResponsePacket(conference.getAgenda()).send(webSocket);
        }
    }
}
