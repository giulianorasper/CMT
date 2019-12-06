package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.admin.GetAllAttendeePasswordsResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAllAttendeePasswordsRequestPacket extends AuthenticatedRequestPacket {

    public GetAllAttendeePasswordsRequestPacket() {
        super(PacketType.GET_ALL_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Packet response = new GetAllAttendeePasswordsResponsePacket(conference.getAllAttendeePasswords());
            response.send(webSocket);
        }
    }
}
