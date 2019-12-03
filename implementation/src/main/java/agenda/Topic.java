package agenda;

import request.Requestable;
import utils.WriterBiasedRWLock;


public class Topic implements Requestable {

    private Agenda subTopics;
    private String name;
    private transient Agenda parent;
    private WriterBiasedRWLock lock; //used for the agenda

    public Topic(String name, Agenda parent){
        this.parent = parent;
        this.name = name;
        this.lock = parent.lock;

        this.subTopics = new Agenda(parent, lock);
    }

    public boolean remove() {
        try {
            lock.getWriteAccess();
            return this.parent.removeTopic(this);
        }
        catch (InterruptedException e){
            return false;
            //do nothing
        }
        finally {
            parent.notifyObservers();
            lock.finishWrite();
        }
    }

    public void rename(String name) {
        try {
            lock.getWriteAccess();
            this.name = name;
            parent.notifyObservers();
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public String getName(){
        try {
            lock.getReadAccess();
            return this.name;
        }
        catch (InterruptedException e){
            //do nothing
            return "";
        }
        finally {
            lock.finishRead();
        }
    }

    public boolean moveToNewAgenda(Agenda agenda, int pos) {
        try {
            lock.getWriteAccess();
            this.parent = agenda;
            return agenda.addTopic(this, pos);
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    public boolean reorder(int pos) {
        try {
            lock.getWriteAccess();
            return this.parent.reOrderTopic(this, pos);
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    public Agenda getSubTopics(){
        try{
            lock.getReadAccess();
            return this.subTopics;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishWrite();
        }
    }

}
