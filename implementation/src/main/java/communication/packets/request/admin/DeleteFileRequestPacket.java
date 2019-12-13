package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;

public class DeleteFileRequestPacket extends AuthenticatedRequestPacket {

    private String name;

    public DeleteFileRequestPacket(PacketType packetType, String name) {
        super(PacketType.DELETE_FILE_REQUEST);
        this.name = name;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            conference.deleteDocument(name);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
