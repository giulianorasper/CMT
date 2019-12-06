package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
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

    public AddVotingRequestPacket(String question, List<String> options, boolean namedVote) {
        super(PacketType.ADD_VOTING_REQUEST_PACKET);
        this.question = question;
        this.options = options;
        this.namedVote = namedVote;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            VotingOption votingOptionObject;
            List<VotingOption> optionsObjectList = new LinkedList<>();
            for(String option : options) {
                if(namedVote) {
                    votingOptionObject = new NamedVotingOption();
                } else {
                    votingOptionObject = new AnonymousVotingOption(0);
                }
                optionsObjectList.add(votingOptionObject);
            }
            Voting voting = new Voting(optionsObjectList, question);
        }
    }
}
