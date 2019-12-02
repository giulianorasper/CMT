package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;

public class NoPermissionResponsePacket extends ResponsePacket {

    public NoPermissionResponsePacket() {
        super(PacketType.NO_PERMISSION, RequestResult.NoPermission);
    }
}
