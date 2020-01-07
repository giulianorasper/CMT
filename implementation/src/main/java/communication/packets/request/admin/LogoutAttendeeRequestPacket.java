package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

/**
 * This packet can be used by an admin to logout a single attendee which is not an admin i.e.
 * the password and token of this attendee will be invalidated. Responds with a {@link communication.packets.BasePacket}.
 */
//TODO dont logout admins
public class LogoutAttendeeRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    /**
     *
     * @param id of the attendee to be logged out
     */
    public LogoutAttendeeRequestPacket(int id) {
        super(PacketType.LOGOUT_ATTENDEE_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.logoutUser(id);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
