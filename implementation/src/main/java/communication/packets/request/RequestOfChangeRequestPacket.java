package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class RequestOfChangeRequestPacket extends AuthenticatedRequestPacket {

    private boolean refersToTopic;
    private String reference;
    private String request;

    public RequestOfChangeRequestPacket(boolean refersToTopic, String reference, String request) {
        super(PacketType.REQUEST_OF_CHANGE_REQUEST);
        this.refersToTopic = refersToTopic;
        this.reference = reference;
        this.request = request;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        new RequestOfPacketWrapper(getPacketType(), refersToTopic, reference, request).setToken(getToken()).handle(conference, webSocket);
    }
}
