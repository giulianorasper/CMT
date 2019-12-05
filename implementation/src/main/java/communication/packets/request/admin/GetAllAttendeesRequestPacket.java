package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAllAttendeesRequestPacket extends AuthenticatedRequestPacket {

    public GetAllAttendeesRequestPacket() {
        super(PacketType.GET_ALL_ATTENDEES_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {

    }
}
