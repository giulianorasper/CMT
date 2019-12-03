package communication.packets.request.admin;

import agenda.Agenda;
import agenda.Topic;
import communication.packets.BasePacket;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.OperationResponse;
import utils.Pair;

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
        Pair<OperationResponse, Agenda> result = conference.getAgenda(getToken());
        if(isPermitted(webSocket, true, result.first())) {
            Agenda agenda = result.second();
            Topic topic = agenda.getTopicFromPreorderString(position);
            topic.rename(name);
        }
    }
}
