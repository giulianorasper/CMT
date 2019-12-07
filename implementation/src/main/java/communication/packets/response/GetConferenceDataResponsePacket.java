package communication.packets.response;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import main.Conference;

public class GetConferenceDataResponsePacket extends ResponsePacket {

    @Expose
    private Conference conference;

    public GetConferenceDataResponsePacket(Conference conference) {
        super(PacketType.CONFERENCE_DATA_RESPONSE, RequestResult.Valid);
        this.conference = conference;
    }
}
