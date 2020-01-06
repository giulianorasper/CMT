package agenda;

import utils.OperationResponse;
import utils.Pair;

public interface AgendaManagement {

    Agenda getAgenda();

    void updateAgenda(Agenda agenda);
}
