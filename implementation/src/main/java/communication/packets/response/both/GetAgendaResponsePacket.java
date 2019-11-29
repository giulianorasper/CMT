package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.request.both.GetAgendaRequestPacket;

/**
 * An response containing the current agenda which is sent as result of an {@link GetAgendaRequestPacket}.
 */
public class GetAgendaResponsePacket extends ResponsePacket {

    //TODO add Agenda attribute as soon as an Agenda class is available

    public GetAgendaResponsePacket() {
        super(PacketType.GET_AGENDA_RESPONSE);
    }
}
