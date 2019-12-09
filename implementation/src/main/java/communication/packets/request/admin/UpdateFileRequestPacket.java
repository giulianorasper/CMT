package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.Pair;

import java.util.HashMap;

public class UpdateFileRequestPacket extends AuthenticatedRequestPacket {

    //TODO lock
    private static HashMap<WebSocket, Pair<UpdateFileRequestPacket, Long>> allowedRequests = new HashMap<>();

    private String name;
    private String originalName;

    public UpdateFileRequestPacket(String name) {
        super(PacketType.UPDATE_FILE_REQUEST);
        this.name = name;
        //this.fileBytes = fileBytes;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            allowedRequests.put(webSocket, new Pair<>(this, System.currentTimeMillis() + 10000));
            new ValidResponsePacket(PacketType.UPDATE_FILE_RESPONSE).send(webSocket);
        }
    }

    public void handleFileTransfer(Conference conference, WebSocket webSocket, byte[] fileBytes) {
        if(isPermitted(conference, webSocket, true)) {
            //TODO use conference to update file
            new ValidResponsePacket().send(webSocket);
        }
    }

    public static UpdateFileRequestPacket getRequestFromConnectionIfExists(WebSocket webSocket) {
        removeInvalidRequests();
        if(allowedRequests.containsKey(webSocket)) return allowedRequests.get(webSocket).first();
        return null;
    }

    private static void removeInvalidRequests() {
        for(WebSocket key : allowedRequests.keySet()) {
            if(allowedRequests.get(key).second() > System.currentTimeMillis()) allowedRequests.remove(key);
        }
    }
}
