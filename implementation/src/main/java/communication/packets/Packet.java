package communication.packets;

import communication.enums.PacketType;
import org.java_websocket.WebSocket;

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
