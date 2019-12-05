package communication.packets.response.admin;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import user.Attendee;

import java.util.List;

public class GetAllAttendeesResponsePacket extends ResponsePacket {

    /**
     * @param attendees a list of all attendees
     * Following properties of the attendee object are exposed:
     * name: String - the attendee's name
     * group: String - the attendee's group
     * function: String - the attendee's function in their group
     * email: String - the attendee's mail address
     * residence: String - the attendee's residence address
     * present: boolean - weather the attendee is present or not
     */
    @Expose
    private List<Attendee> attendees;

    public GetAllAttendeesResponsePacket(RequestResult result, List<Attendee> attendees) {
        super(PacketType.GET_ALL_REQUESTS_RESPONSE, RequestResult.Valid);
        this.attendees = attendees;
    }
}
