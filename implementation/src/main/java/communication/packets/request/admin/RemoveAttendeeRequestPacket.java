package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet can be used by an admin to remove an attendee from the conference.
 */
//TODO dont remove admins, how will this influence requests or similar?
public class RemoveAttendeeRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    /**
     *
     * @param id the id of the attendee to be removed
     */
    public RemoveAttendeeRequestPacket(int id) {
        super(PacketType.REMOVE_ATTENDEE_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.removeAttendee(id);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
