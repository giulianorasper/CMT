package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.DownloadFileResponsePacket;
import document.Document;
import main.Conference;
import org.java_websocket.WebSocket;

import java.io.File;
import java.util.List;

public class DownloadFileRequestPacket extends AuthenticatedRequestPacket {

    private String name;

    public DownloadFileRequestPacket(String name) {
        super(PacketType.DOWNLOAD_FILE_REQUEST);
        this.name = name;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Document document = conference.getDocument(name);
            File file = document.getFile();
            String fileType = "";
            String[] split = file.getName().split("\\.");
            if(split.length >= 2) fileType = "." + split[split.length-1];
            Packet response = new DownloadFileResponsePacket(file, document.getName() + fileType);
            response.send(webSocket);
        }
    }
}
