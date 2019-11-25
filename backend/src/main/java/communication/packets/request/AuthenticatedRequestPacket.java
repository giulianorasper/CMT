package communication.packets.request;

import communication.packets.PacketType;
import communication.packets.RequestPacket;

/**
 * A superclass for packets which require the client to be an authenticated user.
 */
public abstract class AuthenticatedRequestPacket extends RequestPacket {

    private String token;

    public AuthenticatedRequestPacket(PacketType packetType) {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    /**
     *
     * @return An authentication token provided by the sender.
     */
    public String getToken() {
        return token;
    }
}
