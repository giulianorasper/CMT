package agenda;

@SuppressWarnings("checkstyle:typename")
public interface DB_AgendaManagement extends AgendaObserver {

    /**
     *
     * @return the {@link Agenda} object reconstructed from the database.
     */
    Agenda getAgenda();
}
