package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class LogoutAttendeeRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public LogoutAttendeeRequestPacket(int id) {
        super(PacketType.LOGOUT_ATTENDEE_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
