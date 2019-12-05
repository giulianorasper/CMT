package communication.packets.request;

import communication.packets.PacketType;
import communication.packets.RequestPacket;

/**
 * A superclass for packets which require the client to be an authenticated user.
 */
public abstract class AuthenticatedRequestPacket extends RequestPacket {

    private String token;

    public AuthenticatedRequestPacket(PacketType packetType) {
        super(packetType);
    }

    /**
     *
     * @return An authentication token provided by the sender.
     */
    public String getToken() {
        return token;
    }

    public AuthenticatedRequestPacket setToken(String token) {
        this.token = token;
        return this;
    }
}
