package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.admin.GetAllAttendeesResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetAllAttendeesRequestPacket extends AuthenticatedRequestPacket {

    public GetAllAttendeesRequestPacket() {
        super(PacketType.GET_ALL_ATTENDEES_REQUEST);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        Packet response = new GetAllAttendeesResponsePacket(conference.getAllAttendees());
        response.send(webSocket);
    }
}
