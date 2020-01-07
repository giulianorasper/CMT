package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.Voting;
import voting.VotingStatus;

/**
 * This packet can be used by an admin to remove a voting which has not yet been started or ended.
 * Responds with a {@link communication.packets.BasePacket}.
 */
public class RemoveVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public RemoveVotingRequestPacket(int id) {
        super(PacketType.REMOVE_VOTING_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Voting voting = conference.getVoting(id);
            if(voting.getStatus() != VotingStatus.Created) throw new IllegalArgumentException();
            conference.removeVoting(voting);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
