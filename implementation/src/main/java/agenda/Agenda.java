package agenda;

import java.util.LinkedList;
import java.util.List;

public class Agenda implements AgendaObservable{

    private List<Topic> topics;

    void removeTopic(Topic t) {
        this.topics.remove(t);
    }

    void reOrderTopic(Topic t, int pos) {
        //removing topic from old agenda
        t.moveToNewAgenda(this, pos);
    }

    /**
     * TODO: Document this
     *
     * @param agenda String that got extracted from a CSV file, please parse topics using this
     */
    public Agenda(String agenda) {
        //TODO: Implement this

        this.topics = new LinkedList<>();
    }

    public Agenda(){
        this("");
    }

    public boolean addTopic(Topic t, int pos) {
        //No pos >= topics.size() because inserting to the first non existing index is fine
        if(pos >= 0 && pos <= topics.size()){
            return false;
        }

        this.topics.add(pos, t);

        return true;
    }

    public Topic getTopic(int pos) {
        if(pos < 0 || pos >= topics.size()){
            throw new IllegalArgumentException("Invalid position! Was: " + pos);
        }

        return this.topics.get(pos);
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
