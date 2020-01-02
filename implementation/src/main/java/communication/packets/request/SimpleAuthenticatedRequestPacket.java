package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.wrapper.Connection;
import main.Conference;

public class SimpleAuthenticatedRequestPacket extends AuthenticatedRequestPacket {

    public SimpleAuthenticatedRequestPacket(PacketType packetType) {
        super(packetType);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        return;
    }
}
