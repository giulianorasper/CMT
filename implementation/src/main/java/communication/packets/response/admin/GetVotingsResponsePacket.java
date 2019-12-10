package communication.packets.response.admin;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import voting.Voting;

import java.util.List;

public class GetVotingsResponsePacket extends ResponsePacket {

    @Expose
    private List<Voting> votings;

    public GetVotingsResponsePacket(List<Voting> votings) {
        super(PacketType.GET_VOTINGS_RESPONSE, RequestResult.Valid);
        this.votings = votings;
    }
}
