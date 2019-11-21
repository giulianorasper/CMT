package communication.packets.request;

import communication.packets.PacketType;
import communication.packets.RequestPacket;

public abstract class AuthenticatedRequestPacket extends RequestPacket {

    private String token;

    public AuthenticatedRequestPacket(PacketType packetType) {
        super(PacketType.PERSONAL_DATA_REQUEST);
    }

    public String getToken() {
        return token;
    }
}
