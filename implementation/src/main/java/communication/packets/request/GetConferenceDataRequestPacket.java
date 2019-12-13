package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.GetConferenceDataResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetConferenceDataRequestPacket extends AuthenticatedRequestPacket {

    public GetConferenceDataRequestPacket() {
        super(PacketType.CONFERENCE_DATA_REQUEST);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Packet response = new GetConferenceDataResponsePacket(conference);
            response.send(webSocket);
        }
    }
}
