package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import user.Attendee;

public class EditUserRequestPacket extends AuthenticatedRequestPacket {

    private int id;
    private String name;
    private String email;
    private String group;
    private String residence;
    private String function;

    public EditUserRequestPacket(int id, String name, String email, String group, String residence, String function) {
        super(PacketType.EDIT_USER_REQUEST);
        this.id = id;
        this.name = name;
        this.email = email;
        this.group = group;
        this.residence = residence;
        this.function = function;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Attendee attendee = conference.getAttendeeData(id);
            if(name != null) attendee.setName(name);
            if(email != null) attendee.setEmail(email);
            if(group != null) attendee.setGroup(group);
            if(residence != null) attendee.setGroup(residence);
            if(function != null) attendee.setFunction(function);
            conference.editAttendee(attendee);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
