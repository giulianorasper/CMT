package communication.packets.response.attendee;

import com.google.gson.annotations.Expose;
import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;
import communication.packets.request.attendee.GetActiveVotingRequestPacket;
import voting.Voting;

/**
 * A response containing a boolean if there is an active voting. If there is an active voting it contains
 *  more detailed information on a voting. Sent as result of an {@link GetActiveVotingRequestPacket}.
 */
public class GetActiveVotingResponsePacket extends ResponsePacket {

    @Expose
    private boolean exists;
    @Expose
    private Voting voting;

    /**
     * @param exists a boolean indicating if there currently is an active voting
     * @param voting the voting if it exists
     * Following properties of the {@link Voting} object are exposed:
     * id: int - the unique id of the voting
     * named: boolean - if the voting is a named voting
     * question: String - the voting question
     * options: List<VotingOption> - a list of the available voting options
     *
     * The {@link voting.VotingOption} has following exposed properties:
     * name: String - the name of the voting option
     * optionID: int - the unique id of the voting option
     *
     */
    public GetActiveVotingResponsePacket(boolean exists, Voting voting) {
        super(PacketType.GET_ACTIVE_VOTING_RESPONSE, RequestResult.Valid);
        this.exists = exists;
        this.voting = voting;
    }

    public GetActiveVotingResponsePacket(Voting voting) {
        this(true, voting);
    }

    public GetActiveVotingResponsePacket() {
        this(false, null);
    }
}
