package agenda;

import java.util.List;

public class Agenda implements AgendaObservable{

    private List<Topic> topics;

    void removeTopic(Topic t) {
        //TODO: Implement this
    }

    void reOrderTopic(Topic t) {
        //TODO: Implement this
    }

    public Agenda(String agenda) {
        //TODO: Implement this
    }

    public boolean addTopic(Topic t, int pos) {
        return false; //TODO: Implement this
    }

    public Topic getTopic(int pos) {
        return null; //TODO: Implement this
    }

    @Override
    public void register(AgendaObserver o) {
        //TODO: Implement this
    }

    @Override
    public void unregister(AgendaObserver o) {
        //TODO: Implement this
    }

    @Override
    public void notifyObservers() {
        //TODO: Implement this
    }
}
