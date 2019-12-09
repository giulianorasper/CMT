package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.admin.GetVotingsResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetVotingsRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public GetVotingsRequestPacket(int id) {
        super(PacketType.GET_VOTINGS_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Packet response = new GetVotingsResponsePacket(conference.getVotings());
            response.send(webSocket);
        }
    }
}
