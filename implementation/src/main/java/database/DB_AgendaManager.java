package database;

import agenda.Agenda;
import agenda.AgendaObserver;
import agenda.DB_AgendaManagement;
import agenda.Topic;

@SuppressWarnings("checkstyle:typename")
public class DB_AgendaManager extends DB_Controller implements DB_AgendaManagement, AgendaObserver {

    private static String table = "agenda";

    public DB_AgendaManager(String url) {
        super(url);
    }

    @Override
    public boolean update(Agenda a) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean addTopic(Agenda a, Topic t, int pos) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean reorderTopic(Agenda a, Topic t, int newPos) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean removeTopic(Agenda a, Topic t) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean renameTopic(Topic t, String name) {
        return false; //TODO: Implement this
    }

    @Override
    public Agenda getAgenda() {
        return null; //TODO: Implement this
    }
}
