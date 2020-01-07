package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.admin.GetAllAttendeePasswordsResponsePacket;
import communication.wrapper.Connection;
import main.Conference;


public class GetAllAttendeePasswordsRequestPacket extends AuthenticatedRequestPacket {

    public GetAllAttendeePasswordsRequestPacket() {
        super(PacketType.GET_ALL_ATTENDEE_PASSWORDS);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Packet response = new GetAllAttendeePasswordsResponsePacket(conference.getAllUsersPasswords());
            response.send(webSocket);
        }
    }
}
