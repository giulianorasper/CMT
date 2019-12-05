package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class RequestOfSpeechRequestPacket extends AuthenticatedRequestPacket {

    private boolean refersToTopic;
    private String reference;

    public RequestOfSpeechRequestPacket(boolean refersToTopic, String reference) {
        super(PacketType.REQUEST_OF_CHANGE_REQUEST);
        this.refersToTopic = refersToTopic;
        this.reference = reference;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        new RequestOfPacketWrapper(getPacketType(), refersToTopic, reference).setToken(getToken()).handle(conference, webSocket);
    }
}
