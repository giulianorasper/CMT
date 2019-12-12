package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.AnonymousVotingOption;
import voting.NamedVotingOption;
import voting.Voting;
import voting.VotingOption;

import java.util.LinkedList;
import java.util.List;

public class AddVotingRequestPacket extends AuthenticatedRequestPacket {

    private String question;
    private List<String> options;
    private boolean namedVote;
    private int duration;

    public AddVotingRequestPacket(String question, List<String> options, boolean namedVote, int duration) {
        super(PacketType.ADD_VOTING_REQUEST_PACKET);
        this.question = question;
        this.options = options;
        this.namedVote = namedVote;
        this.duration = duration;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            VotingOption votingOptionObject;
            List<VotingOption> optionsObjectList = new LinkedList<>();
            int id = 0;
            for(String option : options) {
                if(namedVote) {
                    votingOptionObject = new NamedVotingOption(id, option);
                } else {
                    votingOptionObject = new AnonymousVotingOption(id, option);
                }
                optionsObjectList.add(votingOptionObject);
                id++;
            }
            Voting voting = new Voting(optionsObjectList, question, namedVote);
            voting.setDuration(duration);
            conference.addVoting(voting);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
