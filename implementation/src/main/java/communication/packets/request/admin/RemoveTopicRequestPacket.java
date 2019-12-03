package communication.packets.request.admin;

import communication.packets.BasePacket;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet handles a remove topic request from an admin and responds with a general {@link BasePacket}.
 */
public class RemoveTopicRequestPacket extends AuthenticatedRequestPacket {

    private String position;

    /**
     *
     * @param position the position of the topic as string (e.g. "1.4.3")
     */
    public RemoveTopicRequestPacket(String position) {
        super(PacketType.REORDER_TOPIC_REQUEST);
        this.position = position;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        //TODO implement
    }
}
