package communication.packets.request.attendee;

import communication.packets.Packet;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import communication.packets.response.attendee.PersonalDataResponsePacket;
import user.Attendee;
import utils.OperationResponse;
import utils.Pair;

/**
 * This packet handles a personal data request from an attendee and responds with an {@link PersonalDataResponsePacket}.
 */
public class PersonalDataRequestPacket extends AuthenticatedRequestPacket {

    public PersonalDataRequestPacket() {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, Attendee> result = conference.getAttendeeData(getToken());
        if(isPermitted(webSocket, false, result.first())) {
            Attendee attendee = result.second();
            Packet response = new PersonalDataResponsePacket(attendee);
            response.send(webSocket);
        }
    }
}
