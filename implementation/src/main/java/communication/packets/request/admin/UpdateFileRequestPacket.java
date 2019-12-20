package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import utils.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class UpdateFileRequestPacket extends AuthenticatedRequestPacket {

    private static ReentrantLock lock = new ReentrantLock();
    private static HashMap<Connection, Pair<UpdateFileRequestPacket, Long>> allowedRequests = new HashMap<>();

    private String name;
    private String originalName;
    private boolean creation;

    public UpdateFileRequestPacket(String name, String originalName) {
        super(PacketType.UPDATE_FILE_REQUEST);
        this.name = name;
        this.originalName = originalName;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            try {
                lock.lock();
                allowedRequests.put(webSocket, new Pair<>(this, System.currentTimeMillis() + 10000));
            } finally {
                lock.unlock();
            }
            new ValidResponsePacket(PacketType.UPDATE_FILE_RESPONSE).send(webSocket);
        }
    }

    public void handleFileTransfer(Conference conference, Connection webSocket, byte[] fileBytes) {
        if(isPermitted(conference, webSocket, true)) {
            String fileType = "";
            String[] split = originalName.split("\\.");
            if(split.length >= 2) fileType = "." + split[split.length-1];
            conference.updateDocument(name, fileType, fileBytes, creation);
            new ValidResponsePacket().send(webSocket);
        }
    }

    public static UpdateFileRequestPacket getRequestFromConnectionIfExists(Connection webSocket) {
        try {
            lock.lock();
            removeInvalidRequests();
            if(allowedRequests.containsKey(webSocket)) {
                UpdateFileRequestPacket packet = allowedRequests.get(webSocket).first();
                allowedRequests.remove(webSocket);
                return packet;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    private static void removeInvalidRequests() {
        try {
            lock.lock();
            new HashSet<>(allowedRequests.keySet()).forEach(key -> {
                if(allowedRequests.get(key).second() < System.currentTimeMillis()) {
                    allowedRequests.remove(key);
                }
            });
        } finally {
            lock.unlock();
        }
    }
}
