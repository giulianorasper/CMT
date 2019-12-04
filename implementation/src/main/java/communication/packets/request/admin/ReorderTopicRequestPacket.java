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

/**
 * This packet handles a reorder topic request from an admin and responds with a general {@link BasePacket}.
 */
public class ReorderTopicRequestPacket extends AuthenticatedRequestPacket {

    private String oldPosition;
    private int newPosition;

    /**
     *
     * @param oldPosition the old position of the topic as string (e.g. "1.4.3")
     * @param newPosition the new position of the topic in the (sub-)agenda
     */
    public ReorderTopicRequestPacket(String oldPosition, int newPosition) {
        super(PacketType.REORDER_TOPIC_REQUEST);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, Agenda> result = conference.getAgenda(getToken());
        if(isPermitted(webSocket, true, result.first())) {
            Agenda agenda = result.second();
            Topic topic = agenda.getTopicFromPreorderString(oldPosition);
            topic.reorder(newPosition);
            new ValidResponsePacket().send(webSocket);
        }
    }
}
