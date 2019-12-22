package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.admin.GetAttendeePasswordResponsePacket;
import communication.wrapper.Connection;
import main.Conference;

public class GetAttendeePasswordRequestPacket extends AuthenticatedRequestPacket {

    int id;

    public GetAttendeePasswordRequestPacket(int id) {
        super(PacketType.GET_ATTENDEE_PASSWORD_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        Packet response = new GetAttendeePasswordResponsePacket(conference.getUserPassword(id).second());
        response.send(webSocket);
    }
}
