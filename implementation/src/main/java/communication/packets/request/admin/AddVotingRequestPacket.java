package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

import java.util.List;

public class AddVotingRequestPacket extends AuthenticatedRequestPacket {

    private String question;
    private List<String> options;
    private boolean namedVote;

    public AddVotingRequestPacket(String question, List<String> options, boolean namedVote) {
        super(PacketType.ADD_VOTING_REQUEST_PACKET);
        this.question = question;
        this.options = options;
        this.namedVote = namedVote;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
