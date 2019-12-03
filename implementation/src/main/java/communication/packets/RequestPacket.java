package communication.packets;

import communication.packets.response.both.FailureResponsePacket;
import communication.packets.response.both.InvalidTokenResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
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

    /**
     * This method has the side effects, that it sends a {@link FailureResponsePacket} or {@link FailureResponsePacket}
     * if the request was not successful.
     * @param socket the socket to cause side effects on
     * @param adminOperation weather to check if the token provides {@link OperationResponse#AdminSuccess}
     * @param value the {@link OperationResponse} to check
     * @return true iff the requesting client has the necessary permissions for the desired request
     */
    public boolean isPermitted(WebSocket socket, boolean adminOperation, OperationResponse value) {
        if(value == OperationResponse.InvalidToken) {
            new InvalidTokenResponsePacket().send(socket);
        } else if(value == OperationResponse.InvalidArguments) {
            new FailureResponsePacket(value.getMessage()).send(socket);
        } else if(value == OperationResponse.AdminSuccess || (value == OperationResponse.AttendeeSuccess && !adminOperation)) {
            return true;
        } else {
            //a token counts as invalid if the associated user does not have the required permissions
            new InvalidTokenResponsePacket().send(socket);
        }
        return false;
    }
}
