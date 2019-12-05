package communication.packets.request.admin;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import user.Attendee;
import utils.OperationResponse;

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
        setNotNull(email);
        setNotNull(group);
        setNotNull(residence);
        setNotNull(function);
        //TODO implement
        //OperationResponse response = conference.addAttendee(getToken(), new Attendee(name, email, group, residence, function));
    }

    private void setNotNull(String s) {
        if(s == null) s = "";
    }
}
