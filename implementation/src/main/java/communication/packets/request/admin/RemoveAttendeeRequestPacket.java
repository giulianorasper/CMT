package communication.packets.request.admin;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class RemoveAttendeeRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public RemoveAttendeeRequestPacket(int id) {
        super(PacketType.REMOVE_ATTENDEE_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
