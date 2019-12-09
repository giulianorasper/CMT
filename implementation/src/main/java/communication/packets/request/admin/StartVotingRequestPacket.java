package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.Voting;

public class StartVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public StartVotingRequestPacket(int id) {
        super(PacketType.START_VOTING_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            //TODO implement
        }
    }
}
