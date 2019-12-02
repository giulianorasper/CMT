package communication.packets.response.both;

import agenda.Agenda;
import communication.packets.PacketType;
import communication.packets.RequestResult;
import communication.packets.ResponsePacket;
import communication.packets.request.both.GetAgendaRequestPacket;

/**
 * An response containing the current agenda which is sent as result of an {@link GetAgendaRequestPacket}.
 */
public class GetAgendaResponsePacket extends ResponsePacket {

    private Agenda agenda;

    public GetAgendaResponsePacket() {
        super(PacketType.GET_AGENDA_RESPONSE, RequestResult.Valid);
    }
}
