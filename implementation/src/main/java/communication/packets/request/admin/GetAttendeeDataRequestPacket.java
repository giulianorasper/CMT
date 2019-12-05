package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAttendeeDataRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GetAttendeeDataRequestPacket(int id) {
        super(PacketType.GET_ATTENDEE_DATA_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
