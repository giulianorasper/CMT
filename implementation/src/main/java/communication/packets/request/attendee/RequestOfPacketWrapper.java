package communication.packets.request.attendee;

import agenda.Agenda;
import communication.packets.PacketType;
import communication.packets.RequestPacket;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.both.ValidResponsePacket;
import document.Document;
import main.Conference;
import org.java_websocket.WebSocket;
import request.ChangeRequest;
import request.Requestable;
import request.SpeechRequest;
import user.Attendee;
import utils.OperationResponse;
import utils.Pair;

public class RequestOfPacketWrapper extends AuthenticatedRequestPacket {

    private boolean refersToTopic;
    private String reference;
    private String request;
    private boolean isSpeechRequest = false;

    public RequestOfPacketWrapper(PacketType packetType, boolean refersToTopic, String reference, String request) {
        super(packetType);
        this.refersToTopic = refersToTopic;
        this.reference = reference;
        this.request = request;
    }

    public RequestOfPacketWrapper(PacketType packetType, boolean refersToTopic, String reference) {
        super(packetType);
        this.refersToTopic = refersToTopic;
        this.reference = reference;
        this.isSpeechRequest = true;
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Requestable requestable;
        if(refersToTopic) {
            Pair<OperationResponse, Agenda> result = conference.getAgenda(getToken());
            if(!isPermitted(webSocket, false, result.first())) return;
            requestable = result.second().getTopicFromPreorderString(reference);
        } else {
            Pair<OperationResponse, Document> result = conference.getDocument(getToken(), reference);
            if(!isPermitted(webSocket, false, result.first())) return;
            requestable = result.second();
        }
        Pair<OperationResponse, Attendee> result = conference.getAttendeeData(getToken());
        if(!isPermitted(webSocket, false, result.first())) return;
        OperationResponse response;
        if(isSpeechRequest) {
            response = conference.addRequest(getToken(), new SpeechRequest(result.second(), requestable, System.currentTimeMillis()));
        } else {
            response = conference.addRequest(getToken(), new ChangeRequest(result.second(), requestable, System.currentTimeMillis(), request));
        }
        if(isPermitted(webSocket, false, response)) {
            new ValidResponsePacket().send(webSocket);
        }
    }
}
