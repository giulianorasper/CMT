package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import user.Attendee;

public class AddAttendeeRequestPacket extends AuthenticatedRequestPacket {

    private String name;
    private String email;
    private String group;
    private String residence;
    private String function;

    public AddAttendeeRequestPacket(String name, String email, String group, String residence, String function) {
        super(PacketType.ADD_ATTENDEE_REQUEST);
        this.name = name;
        this.email = email;
        this.group = group;
        this.residence = residence;
        this.function = function;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            setNotNull(email);
            setNotNull(group);
            setNotNull(residence);
            setNotNull(function);
            Attendee attendee = new Attendee(name, conference.getFreeUserName(name), email, group, residence, function);
            conference.addAttendee(attendee);
            new ValidResponsePacket().send(webSocket);
        }
    }

    private void setNotNull(String s) {
        if(s == null) s = "";
    }
}
