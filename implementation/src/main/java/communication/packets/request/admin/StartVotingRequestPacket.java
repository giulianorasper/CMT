package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.Voting;
import voting.VotingStatus;

public class StartVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    public StartVotingRequestPacket(int id) {
        super(PacketType.START_VOTING_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Voting activeVoting = conference.getActiveVoting();
            Voting votingToStart = conference.getVoting(id);
            if(activeVoting == null && votingToStart.getStatus() == VotingStatus.Created) {
                votingToStart.startVote();
                conference.update(votingToStart);
            }
            new ValidResponsePacket().send(webSocket);
        }
    }
}
