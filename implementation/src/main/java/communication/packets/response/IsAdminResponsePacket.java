package communication.packets.response;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;

public class IsAdminResponsePacket extends ResponsePacket {

    @Expose
    private boolean admin;

    public IsAdminResponsePacket(boolean admin) {
        super(PacketType.IS_ADMIN_RESPONSE, RequestResult.Valid);
        this.admin = admin;
    }
}
