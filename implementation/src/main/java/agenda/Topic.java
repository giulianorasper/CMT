package agenda;

import com.google.gson.annotations.Expose;
import request.Requestable;

public class Topic implements Requestable {

    @Expose
    private Agenda subTopics;
    @Expose
    private String name;
    private transient Agenda parent;

    public Topic(String name, Agenda parent){
        this.parent = parent;
        this.name = name;

        this.subTopics = new Agenda();
    }

    public void remove() {
        this.parent.removeTopic(this);
    }

    public void rename(String name) {
        this.name = name;
        //TODO: notify observers
    }

    public String getName(){
        return this.name;
    }

    public void moveToNewAgenda(Agenda agenda, int pos) {
        this.parent = agenda;

        agenda.addTopic(this, pos);
    }

    public void reorder(int pos) {
        this.parent.reOrderTopic(this, pos);
    }

    public Agenda getSubTopics(){
        return this.subTopics;
    }

}
