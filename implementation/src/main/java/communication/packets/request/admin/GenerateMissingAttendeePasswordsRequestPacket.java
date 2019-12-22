package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

public class GenerateMissingAttendeePasswordsRequestPacket extends AuthenticatedRequestPacket {

    public GenerateMissingAttendeePasswordsRequestPacket() {
        super(PacketType.GENERATE_MISSING_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.generateAllMissingUserPasswords();
            new ValidResponsePacket().send(webSocket);
        }
    }
}
