package communication.packets.response;

import agenda.Agenda;
import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import communication.packets.request.GetAgendaRequestPacket;

/**
 * An response containing the current agenda which is sent as result of an {@link GetAgendaRequestPacket}.
 */
public class GetAgendaResponsePacket extends ResponsePacket {

    @Expose
    private Agenda agenda;

    public GetAgendaResponsePacket(Agenda agenda) {
        super(PacketType.GET_AGENDA_RESPONSE, RequestResult.Valid);
        this.agenda = agenda;
    }
}
