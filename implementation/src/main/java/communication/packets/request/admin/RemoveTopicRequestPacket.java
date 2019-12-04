package communication.packets.request.admin;

import agenda.Agenda;
import agenda.Topic;
import communication.packets.BasePacket;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.both.ValidResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.OperationResponse;
import utils.Pair;

import java.util.LinkedList;
import java.util.List;

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
        Pair<OperationResponse, Agenda> result = conference.getAgenda(getToken());
        if(isPermitted(webSocket, true, result.first())) {
            Agenda agenda = result.second();
            Topic topic = agenda.getTopicFromPreorderString(position);
            topic.remove();
            new ValidResponsePacket().send(webSocket);
        }
    }
}
