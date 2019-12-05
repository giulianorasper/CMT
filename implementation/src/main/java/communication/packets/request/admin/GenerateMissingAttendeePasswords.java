package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GenerateMissingAttendeePasswords extends AuthenticatedRequestPacket {

    public GenerateMissingAttendeePasswords() {
        super(PacketType.GENERATE_MESSING_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
