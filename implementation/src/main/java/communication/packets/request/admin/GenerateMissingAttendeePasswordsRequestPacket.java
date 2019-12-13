package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class GenerateMissingAttendeePasswordsRequestPacket extends AuthenticatedRequestPacket {

    public GenerateMissingAttendeePasswordsRequestPacket() {
        super(PacketType.GENERATE_MESSING_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.generateAllMissingAttendeePasswords();
            new ValidResponsePacket().send(webSocket);
        }
    }
}
