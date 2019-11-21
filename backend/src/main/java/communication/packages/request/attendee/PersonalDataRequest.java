package communication.packages.request.attendee;

import communication.CommunicationFactory;
import communication.packages.PacketType;
import communication.packages.request.AuthenticatedRequestPacket;
import org.java_websocket.WebSocket;

public class PersonalDataRequest extends AuthenticatedRequestPacket {

    public PersonalDataRequest() {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    @Override
    public void handle(CommunicationFactory factory, WebSocket webSocket) {
    }
}
