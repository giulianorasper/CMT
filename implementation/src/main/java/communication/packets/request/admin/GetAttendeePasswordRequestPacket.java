package communication.packets.request.admin;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAttendeePasswordRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GetAttendeePasswordRequestPacket(int id) {
        super(PacketType.GET_ATTENDEE_PASSWORD_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
