package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

public class GenerateNewAttendeeTokenRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GenerateNewAttendeeTokenRequestPacket(int id) {
        super(PacketType.GENERATE_NEW_ATTENDEE_TOKEN);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.generateNewUserToken(id);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
