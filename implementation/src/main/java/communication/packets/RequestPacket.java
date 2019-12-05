package communication.packets;

import communication.packets.response.both.FailureResponsePacket;
import communication.packets.response.both.InvalidTokenResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import user.TokenResponse;
import utils.OperationResponse;

/**
 * An extension of {@link BasePacket} which handles requests of clients by running the {@link RequestPacket#handle(Conference, WebSocket)} method.
 */
public abstract class RequestPacket extends BasePacket {

    public RequestPacket(PacketType packetType) {
        super(packetType);
    }

    //TODO try catch?
    /**
     * A method which is called on any incoming packet.
     * Each subclass calls this method to handle it's associated request properly.
     * @param conference An {@link Conference} which enables handling the request by calling provided backend methods.
     * @param webSocket An {@link WebSocket} to send an {@link ResponsePacket} to if required.
     */
    public abstract void handle(Conference conference, WebSocket webSocket);
}
