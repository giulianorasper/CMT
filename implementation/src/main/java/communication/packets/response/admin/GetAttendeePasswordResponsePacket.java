package communication.packets.response.admin;

import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;

public class GetAttendeePasswordResponsePacket extends ResponsePacket {

    private String password;

    public GetAttendeePasswordResponsePacket(String password) {
        super(PacketType.GET_ATTENDEE_PASSWORD_RESPONSE, RequestResult.Valid);
        this.password = password;
    }
}
