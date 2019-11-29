package agenda;

import utils.OperationResponse;
import utils.Pair;

public interface AgendaManagement {

    Pair<OperationResponse, Agenda> getAgenda(String token);
}
