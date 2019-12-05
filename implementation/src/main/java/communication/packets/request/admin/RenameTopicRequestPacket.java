package communication.packets.request.admin;

import agenda.Agenda;
import agenda.Topic;
import communication.packets.BasePacket;
import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;

/**
 * This packet handles a rename topic request from an admin and responds with a general {@link BasePacket}.
 */
public class RenameTopicRequestPacket extends AuthenticatedRequestPacket {

    private String position;
    private String name;

    /**
     *
     * @param position the position of the topic as string (e.g. "1.4.3")
     * @param name the new name for the topic
     */
    public RenameTopicRequestPacket(String position, String name) {
        super(PacketType.RENAME_TOPIC_REQUEST);
        this.position = position;
        this.name = name;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            Agenda agenda = conference.getAgenda();
            Topic topic = agenda.getTopicFromPreorderString(position);
            topic.rename(name);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
