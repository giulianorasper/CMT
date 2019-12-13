package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
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
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            if(name == null) throw new IllegalArgumentException();
            Attendee attendee = new Attendee(name, email, conference.getFreeUserName(name), group, residence, function);
            conference.addAttendee(attendee);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
