package communication.packets.request.attendee;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import communication.packets.response.attendee.PersonalDataResponse;

/**
 * This packet handles a personal data request from an attendee and responds with an {@link PersonalDataResponse}.
 */
public class PersonalDataRequest extends AuthenticatedRequestPacket {

    public PersonalDataRequest() {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
