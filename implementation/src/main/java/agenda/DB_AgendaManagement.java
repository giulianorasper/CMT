package agenda;

@SuppressWarnings("checkstyle:typename")
public interface DB_AgendaManagement extends AgendaObserver {

    Agenda getAgenda();
}
