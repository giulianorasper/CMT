package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

public class SetRequestApprovalStatusRequestPacket extends AuthenticatedRequestPacket {

    private int id;
    private int approved;

    public SetRequestApprovalStatusRequestPacket(int id, int approved) {
        super(PacketType.SET_REQUEST_APPROVAL_STATUS);
        this.id = id;
        this.approved = approved;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
