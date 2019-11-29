package communication.packets;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

/**
 * An interface representing a data packet which can be sent through an open {@link WebSocket} connection.
 */
public interface Packet {

    /**
     *
     * @return The {@link PacketType} of the packet.
     */
    public PacketType getPacketType();

    /**
     * Sends this packet through an open {@link WebSocket} connection.
     * @param socket the socket to send data through
     */
    public void send(WebSocket socket);
}
