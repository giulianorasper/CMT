package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.GetDocumentListResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class GetDocumentListRequestPacket extends AuthenticatedRequestPacket {

    public GetDocumentListRequestPacket() {
        super(PacketType.GET_DOCUMENT_LIST_REQUEST);
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Packet response = new GetDocumentListResponsePacket(conference.getAllDocuments());
            response.send(webSocket);
        }
    }
}
