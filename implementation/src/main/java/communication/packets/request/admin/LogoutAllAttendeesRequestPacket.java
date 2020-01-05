package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

public class LogoutAllAttendeesRequestPacket extends AuthenticatedRequestPacket {

    public LogoutAllAttendeesRequestPacket() {
        super(PacketType.LOGOUT_ALL_ATTENDEES);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.logoutAllUsers();
            new ValidResponsePacket().send(webSocket);
        }
    }
}
