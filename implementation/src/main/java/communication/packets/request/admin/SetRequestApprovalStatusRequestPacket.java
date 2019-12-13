package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import request.ChangeRequest;
import request.Request;

public class SetRequestApprovalStatusRequestPacket extends AuthenticatedRequestPacket {

    private int id;
    private boolean approved;

    public SetRequestApprovalStatusRequestPacket(int id, boolean approved) {
        super(PacketType.SET_REQUEST_APPROVAL_STATUS);
        this.id = id;
        this.approved = approved;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Request request = conference.getRequest(id);
            if(request instanceof ChangeRequest) {
                ChangeRequest changeRequest = (ChangeRequest) request;
                if(approved) {
                    changeRequest.approve();
                } else {
                    changeRequest.disapprove();
                }
                new ValidResponsePacket().send(webSocket);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
