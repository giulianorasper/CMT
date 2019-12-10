package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import voting.*;

import java.util.LinkedList;
import java.util.List;

public class EditVotingRequestPacket extends AuthenticatedRequestPacket {

    private int id;
    private String question;
    private List<String> options;
    private int duration;

    public EditVotingRequestPacket(String question, List<String> options, int duration) {
        super(PacketType.EDIT_VOTING_REQUEST);
        this.question = question;
        this.options = options;
        this.duration = duration;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Voting voting = conference.getVoting(id);
            if(voting.getStatus() != VotingStatus.Created) throw new IllegalArgumentException();
            if(question == null) question = voting.getQuestion();
            if(options == null) {
                options = new LinkedList<>();
                voting.getOptions().forEach(o -> {
                    options.add(o.getName());
                });
            }
            boolean namedVote = voting.isNamedVote();
            AddVotingRequestPacket add = new AddVotingRequestPacket(question, options, namedVote, duration);
            add.handle(conference, webSocket);
            conference.removeVoting(voting);
        }
    }
}
