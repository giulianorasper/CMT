package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

public class GenerateNewAttendeePasswordRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GenerateNewAttendeePasswordRequestPacket(int id) {
        super(PacketType.GENERATE_NEW_ATTENDEE_PASSWORD);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.generateNewUserPassword(id);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
