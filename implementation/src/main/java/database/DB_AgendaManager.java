package database;

import agenda.Agenda;
import agenda.AgendaObserver;
import agenda.DB_AgendaManagement;
import agenda.Topic;

public class DB_AgendaManager extends DB_Controller implements DB_AgendaManagement, AgendaObserver {

    @Override
    public void init() {
        //TODO: Implement this
    }

    @Override
    public void openConnection() {
        //TODO: Implement this
    }

    @Override
    public void closeConnection() {
        //TODO: Implement this
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
