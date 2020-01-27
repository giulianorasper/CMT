package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.Packet;
import communication.packets.response.DownloadFileResponsePacket;
import communication.wrapper.Connection;
import document.Document;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.Pair;

import java.io.File;
import java.util.List;

/**
 * This packet can be used by an attendee to request a file (document) download.
 * Responds with a {@link DownloadFileResponsePacket}.
 */
public class DownloadFileRequestPacket extends AuthenticatedRequestPacket {

    private String name;

    /**
     *
     * @param name the name of the file to download
     */
    public DownloadFileRequestPacket(String name) {
        super(PacketType.DOWNLOAD_FILE_REQUEST);
        this.name = name;
    }

    @Override
    public void handle(Conference conference, Connection connection) {
        if(isPermitted(conference, connection, false)) {
            File document = conference.getDocumentContentAsFile(name);
            connection.sendFile(document, name);
        }
    }
}
