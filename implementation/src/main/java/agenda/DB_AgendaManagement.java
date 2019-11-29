package agenda;

public interface DB_AgendaManagement {

    boolean addTopic(Agenda a, Topic t, int pos);

    boolean reorderTopic(Agenda a, Topic t, int newPos);

    boolean removeTopic(Agenda a, Topic t);

    boolean renameTopic(Topic t, String name);

    Agenda getAgenda();
}
