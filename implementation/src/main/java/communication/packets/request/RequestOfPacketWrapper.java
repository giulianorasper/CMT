package communication.packets.request;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import request.ChangeRequest;
import request.Requestable;
import request.SpeechRequest;
import user.User;

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
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, false)) {
            Requestable requestable;
            if(refersToTopic) {
                requestable = conference.getAgenda().getTopicFromPreorderString(reference);
            } else {
                requestable = conference.getDocument(reference);
            }
            User requester = conference.getAttendeeData(conference.tokenToID(getToken()));

            if(isSpeechRequest) {
                conference.addRequest(new SpeechRequest(requester, requestable, System.currentTimeMillis() / 1000));
            } else {
                conference.addRequest(new ChangeRequest(requester, requestable, System.currentTimeMillis() / 1000, request));
            }
            new ValidResponsePacket().send(webSocket);
        }
    }
}
