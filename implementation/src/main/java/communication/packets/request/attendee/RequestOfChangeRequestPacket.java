package communication.packets.request.attendee;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class RequestOfChangeRequestPacket extends AuthenticatedRequestPacket {

    private boolean refersToTopic;
    //TODO topic / document
    private String request;

    public RequestOfChangeRequestPacket(PacketType packetType, boolean refersToTopic, String request) {
        super(packetType);
        this.refersToTopic = refersToTopic;
        this.request = request;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO implement as soon as enough backend functionality is available
    }
}
