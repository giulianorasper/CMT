package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.IsAdminResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class IsAdminRequestPacket extends AuthenticatedRequestPacket {

    public IsAdminRequestPacket() {
        super(PacketType.IS_ADMIN_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Packet response = new IsAdminResponsePacket(conference.isAdmin(conference.tokenToID(getToken())));
            response.send(webSocket);
        }
    }
}
