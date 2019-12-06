package communication.packets.response.admin;

import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import user.User;
import utils.Pair;

import java.util.List;

public class GetAllAttendeePasswordsResponsePacket extends ResponsePacket {

    private List<Pair<User, String>> passwords;

    public GetAllAttendeePasswordsResponsePacket(List<Pair<User, String>> passwords) {
        super(PacketType.GET_ALL_ATTENDEE_PASSWORDS_RESPONSE, RequestResult.Valid);
        this.passwords = passwords;
    }
}
