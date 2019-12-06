package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import user.Attendee;

public class SetAttendeePresentStatusRequestPacket extends AuthenticatedRequestPacket {

    int id;
    boolean present;

    public SetAttendeePresentStatusRequestPacket(int id, boolean present) {
        super(PacketType.SET_ATTENDEE_PRESENT_STATUS_REQUEST);
        this.id = id;
        this.present = present;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Attendee attendee = conference.getAttendeeData(id);
            attendee.setPresent(present);
            conference.editAttendee(attendee);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
