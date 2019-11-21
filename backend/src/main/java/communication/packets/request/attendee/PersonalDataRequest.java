package communication.packets.request.attendee;

import communication.CommunicationFactory;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import org.java_websocket.WebSocket;

public class PersonalDataRequest extends AuthenticatedRequestPacket {

    public PersonalDataRequest() {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    @Override
    public void handle(CommunicationFactory factory, WebSocket webSocket) {
    }
}
