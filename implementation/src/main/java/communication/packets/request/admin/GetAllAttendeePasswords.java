package communication.packets.request.admin;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAllAttendeePasswords extends AuthenticatedRequestPacket {

    public GetAllAttendeePasswords() {
        super(PacketType.GET_ALL_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
