package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GenerateNewAttendeeTokenRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GenerateNewAttendeeTokenRequestPacket(int id) {
        super(PacketType.GENERATE_NEW_ATTENDEE_TOKEN);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
