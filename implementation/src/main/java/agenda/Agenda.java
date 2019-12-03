package agenda;

import com.google.gson.annotations.Expose;

import utils.WriterBiasedRWLock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Agenda implements AgendaObservable{

    private Agenda parent;
    @Expose
    private List<Topic> topics;
    protected WriterBiasedRWLock lock;
    private ConcurrentHashMap<AgendaObserver, Boolean> observers = new ConcurrentHashMap<>(); // a map backed hashset

    boolean removeTopic(Topic t) {
        return this.topics.remove(t);
    }

    boolean reOrderTopic(Topic t, int pos) {
        return t.moveToNewAgenda(this, pos);
    }

    /**
     * TODO: Document this
     *
     * @param agenda String that got extracted from a CSV file, please parse topics using this
     */
    public Agenda(String agenda) {
        //TODO: Implement this

        this.topics = new LinkedList<>();
        this.lock = new WriterBiasedRWLock();
    }

    public Agenda(){
        this("");
    }

    protected Agenda (Agenda parent, WriterBiasedRWLock lock){
        this("");
        this.lock = lock;
        this.parent = parent;
    }

    public boolean addTopic(Topic t, int pos) {
        try {
            lock.getWriteAccess();
            if (pos >= 0 && pos <= topics.size()) {
                this.topics.add(pos, t);
                return true;
            }
            return false;
        }
        catch (InterruptedException e){
            //do nothing
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    public Topic getTopic(int pos) {
        try {
            lock.getReadAccess();
            if (pos < 0 || pos >= topics.size()) {
                throw new IllegalArgumentException("Invalid position! Was: " + pos);
            }

            return this.topics.get(pos);
        }
        catch (InterruptedException e){
            // do nothing
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public Topic getTopicFromPreorderString(String preorder) {
        String[] preorderArray = preorder.split("\\.");
        List<Integer> preorderList = new LinkedList<>();
        for(String s : preorderArray) {
            //here we convert counting from 1 to counting from 0
            preorderList.add((Integer.parseInt(s)-1));
        }
        return getTopicFromPreorderList(preorderList);
    }

    protected Topic getTopicFromPreorderList(List<Integer> preorder) {
        try {
            Topic topic = topics.get(preorder.get(0));
            preorder.remove(0);
            return topic.getTopicFromPreorderList(preorder);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void register(AgendaObserver o) {
        observers.put(o, true);
    }

    @Override
    public void unregister(AgendaObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        if(parent == null) {
            observers.forEachKey(2, o -> o.update(this));
        }
        else{
            parent.notifyObservers();
        }
    }
}
