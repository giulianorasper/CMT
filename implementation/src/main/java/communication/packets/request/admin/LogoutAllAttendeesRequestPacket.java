package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class LogoutAllAttendeesRequestPacket extends AuthenticatedRequestPacket {

    public LogoutAllAttendeesRequestPacket() {
        super(PacketType.LOGOUT_ALL_ATTENDEES);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.logoutAllAttendees();
            new ValidResponsePacket().send(webSocket);
        }
    }
}
