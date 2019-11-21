package communication.packages.request;

import communication.packages.PacketType;
import communication.packages.RequestPacket;

public abstract class AuthenticatedRequestPacket extends RequestPacket {

    private String token;

    public AuthenticatedRequestPacket(PacketType packetType) {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    public String getToken() {
        return token;
    }
}
