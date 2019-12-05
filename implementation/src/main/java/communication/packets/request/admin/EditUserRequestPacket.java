package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class EditUserRequestPacket extends AuthenticatedRequestPacket {

    private String name;
    private String email;
    private String group;
    private String residence;
    private String function;

    public EditUserRequestPacket(String name, String email, String group, String residence, String function) {
        super(PacketType.EDIT_USER_REQUEST);
        this.name = name;
        this.email = email;
        this.group = group;
        this.residence = residence;
        this.function = function;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO implement
    }
}
