package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
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
        //TODO handle
    }
}
