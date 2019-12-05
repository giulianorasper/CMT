package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class RemoveVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public RemoveVotingRequestPacket(int id) {
        super(PacketType.REMOVE_VOTING_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
