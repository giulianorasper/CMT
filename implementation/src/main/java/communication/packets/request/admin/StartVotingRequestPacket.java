package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.FailureResponsePacket;
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
            Packet response;
            Voting activeVoting = conference.getActiveVoting();
            Voting votingToStart = conference.getVoting(id);
            if(votingToStart == null) {
                response = new FailureResponsePacket("The voting with the id " + id + " does not exist.");
            } else if (votingToStart.getStatus() != VotingStatus.Created){
                response = new FailureResponsePacket("Voting could not be started since it's status is " + votingToStart.getStatus());
            } else if(activeVoting != null) {
                response = new FailureResponsePacket("Voting could not be started since there is already an ongoing voting.");
            }
            else {
                votingToStart.startVote();
                conference.update(votingToStart);
                response = new ValidResponsePacket();
            }
            response.send(webSocket);
        }
    }
}
