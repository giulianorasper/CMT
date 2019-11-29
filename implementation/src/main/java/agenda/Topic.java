package agenda;

import request.Requestable;

public class Topic implements Requestable {

    private Agenda subTopics;
    private String name;
    private Agenda parent;

    public void remove() {
        //TODO: Implement this
    }

    public void rename(String name) {
        //TODO: Implement this
        //TODO: notify observers
    }

    public void moveToNewAgenda(Agenda agenda, int pos) {
        //TODO: Implement this
    }

    public void reorder(int pos) {
        //TODO: Implement this
    }
}
