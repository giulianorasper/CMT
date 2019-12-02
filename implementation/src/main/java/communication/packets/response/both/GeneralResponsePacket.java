package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.ResponsePacket;

/**
 * A general response sent as result of a request {@link ResponsePacket} providing no further information.
 * This packet only indicates a client that their request was processed in some way.
 */
public class GeneralResponsePacket extends ResponsePacket {

    public GeneralResponsePacket() {
        super(PacketType.GENERAL_RESPONSE);
    }
}
