package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.Pair;

import java.util.HashMap;
import java.util.HashSet;

public class UpdateFileRequestPacket extends AuthenticatedRequestPacket {

    //TODO lock
    private static HashMap<WebSocket, Pair<UpdateFileRequestPacket, Long>> allowedRequests = new HashMap<>();

    private String name;
    private String originalName;
    private boolean creation;

    public UpdateFileRequestPacket(String name, String originalName) {
        super(PacketType.UPDATE_FILE_REQUEST);
        this.name = name;
        this.originalName = originalName;
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
            String fileType = "";
            String[] split = originalName.split("\\.");
            if(split.length >= 2) fileType = "." + split[split.length-1];
            conference.updateDocument(name, fileType, fileBytes, creation);
            new ValidResponsePacket().send(webSocket);
        }
    }

    public static UpdateFileRequestPacket getRequestFromConnectionIfExists(WebSocket webSocket) {
        removeInvalidRequests();
        if(allowedRequests.containsKey(webSocket)) return allowedRequests.get(webSocket).first();
        return null;
    }

    private static void removeInvalidRequests() {
        new HashSet<>(allowedRequests.keySet()).forEach(key -> {
            if(allowedRequests.get(key).second() > System.currentTimeMillis()) allowedRequests.remove(key);
        });
    }
}
