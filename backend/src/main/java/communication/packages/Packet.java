package communication.packages;

import org.java_websocket.WebSocket;

public interface Packet {

    public PacketType getPacketType();

    public void send(WebSocket socket);
}
