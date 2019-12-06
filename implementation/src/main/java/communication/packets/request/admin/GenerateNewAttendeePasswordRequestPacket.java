package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GenerateNewAttendeePasswordRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GenerateNewAttendeePasswordRequestPacket(int id) {
        super(PacketType.GENERATE_NEW_ATTENDEE_PASSWORD);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.generateNewAttendeePassword(id);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
