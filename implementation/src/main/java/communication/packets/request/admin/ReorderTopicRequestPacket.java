package communication.packets.request.admin;

import communication.packets.BasePacket;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet handles a reorder topic request from an admin and responds with a general {@link BasePacket}.
 */
public class ReorderTopicRequestPacket extends AuthenticatedRequestPacket {

    private String oldPosition;
    private String newPosition;

    /**
     *
     * @param oldPosition the old position of the topic as string (e.g. "1.4.3")
     * @param newPosition the new position of the topic as string (e.g. "1.4.3")
     */
    public ReorderTopicRequestPacket(String oldPosition, String newPosition) {
        super(PacketType.REORDER_TOPIC_REQUEST);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO handle
    }
}
