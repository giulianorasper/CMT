package communication.packets.request.admin;

import communication.packets.PacketType;
import communication.packets.request.AuthenticatedRequestPacket;
import communication.packets.response.admin.GetAllRequestsResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import request.Request;
import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public class GetAllRequestsRequestPacket extends AuthenticatedRequestPacket {

    public GetAllRequestsRequestPacket() {
        super(PacketType.GET_ALL_REQUESTS_REQUEST);
    }

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<OperationResponse, List<Request>> result = conference.getAllRequests(getToken());
        if(isPermitted(webSocket, true, result.first())) {
            new GetAllRequestsResponsePacket(result.second()).send(webSocket);
        }
    }
}
