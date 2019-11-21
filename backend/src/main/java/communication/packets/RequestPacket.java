package communication.packets;

import communication.CommunicationFactory;
import org.java_websocket.WebSocket;

public abstract class RequestPacket extends BasePacket {

    public RequestPacket(PacketType packetType) {
        super(packetType);
    }

    public abstract void handle(CommunicationFactory factory, WebSocket webSocket);
}
