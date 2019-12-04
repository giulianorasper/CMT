package communication.packets.request.admin;

import agenda.Agenda;
import agenda.Topic;
import com.google.gson.internal.$Gson$Preconditions;
import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.BasePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import utils.OperationResponse;
import utils.Pair;

/**
 * This packet handles an add topic request from an admin and responds with a general {@link BasePacket}.
 */
public class AddTopicRequestPacket extends AuthenticatedRequestPacket {

    private String position;
    private String name;

    /**
     *
     * @param position the position of the topic as string (e.g. "1.4.3")
     * @param name the name of the topic
     */
    public AddTopicRequestPacket(String position, String name) {
        super(PacketType.ADD_TOPIC_REQUEST);
        this.position = position;
        this.name = name;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, Agenda> result = conference.getAgenda(getToken());
        if(isPermitted(webSocket, true, result.first())) {
            Agenda mainAgenda = result.second();
            Agenda agenda = mainAgenda.getAgendaFromPreorderString(position);
            Topic topic = new Topic(name, mainAgenda);
            //agenda.addTopic(mainAgenda)
        }
    }
}
