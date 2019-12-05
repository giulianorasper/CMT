package communication.packets.response.admin;

import com.google.gson.annotations.Expose;
import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;
import request.Request;

import java.util.List;

public class GetAllRequestsResponsePacket extends ResponsePacket {

    //TODO packet content information
    @Expose
    List<Request> requests;

    public GetAllRequestsResponsePacket(List<Request> requests) {
        super(PacketType.GET_ALL_REQUESTS_RESPONSE, RequestResult.Valid);
        this.requests = requests;
    }
}
