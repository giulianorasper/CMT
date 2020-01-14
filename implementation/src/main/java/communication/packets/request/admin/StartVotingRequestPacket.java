package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.FailureResponsePacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import voting.Voting;
import voting.VotingStatus;

/**
 * This packet can be used by an admin to start a voting which not already started or ended.
 * Respond with a {@link communication.packets.BasePacket}.
 */
public class StartVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;

    /**
     *
     * @param id the id of the voting to be started
     */
    public StartVotingRequestPacket(int id) {
        super(PacketType.START_VOTING_REQUEST);
        this.id = id;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Packet response;
            Voting votingToStart = conference.getVoting(id);
            if(votingToStart == null) {
                response = new FailureResponsePacket("The voting with the id " + id + " does not exist.");
            } else if (votingToStart.getStatus() != VotingStatus.Created){
                response = new FailureResponsePacket("Voting could not be started since it's status is " + votingToStart.getStatus());
            } else {
                conference.startVoting(votingToStart);
                response = new ValidResponsePacket();
            }
            response.send(webSocket);
        }
    }
}
