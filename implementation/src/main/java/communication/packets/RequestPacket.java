package communication.packets;

import communication.packets.response.both.FailureResponsePacket;
import communication.packets.response.both.InvalidTokenResponsePacket;
import communication.packets.response.both.NoPermissionResponsePacket;
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

    /**
     * A method which is called on any incoming packet.
     * Each subclass calls this method to handle it's associated request properly.
     * @param conference An {@link Conference} which enables handling the request by calling provided backend methods.
     * @param webSocket An {@link WebSocket} to send an {@link ResponsePacket} to if required.
     */
    public abstract void handle(Conference conference, WebSocket webSocket);

    /**
     * -- May not remain in the final implementation due to adjustments to match the may have requirements --
     * This method has the side effects, that it sends a {@link FailureResponsePacket}, {@link NoPermissionResponsePacket} or {@link FailureResponsePacket}
     * if the request was not successful.
     * @param socket
     * @param expected
     * @param value
     * @return true iff the requesting client has the necessary permissions for the desired request
     */
    public boolean isPermitted(WebSocket socket, PermissionLevel expected, OperationResponse value) {
        if(value == OperationResponse.InvalidToken) {
            new InvalidTokenResponsePacket().send(socket);
        } else if(value == OperationResponse.InvalidArguments) {
            new FailureResponsePacket();
        } else if(!(value == OperationResponse.AdminSuccess && (expected == PermissionLevel.Admin || expected == PermissionLevel.AdminOrAttendee) || value == OperationResponse.AttendeeSuccess && (expected == PermissionLevel.Admin || expected == PermissionLevel.AdminOrAttendee))) {
            new NoPermissionResponsePacket();
        } else {
            return true;
        }
        return false;
    }
}
