package communication.packets.response.both;

import com.google.gson.annotations.Expose;
import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;

public class FailureResponsePacket extends ResponsePacket {

    @Expose
    private String details;

    public FailureResponsePacket(String details) {
        super(PacketType.FAILURE, RequestResult.Failure);
        this.details = details;
    }

    public FailureResponsePacket() {
        this(null);
    }
}
