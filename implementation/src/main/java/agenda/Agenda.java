package agenda;

import com.google.gson.annotations.Expose;

import utils.LexicographicalComparator;
import utils.Pair;
import utils.WriterBiasedRWLock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Agenda implements AgendaObservable{

    private Agenda parent;
    @Expose
    private List<Topic> topics;
    protected WriterBiasedRWLock lock;
    private ConcurrentHashMap<AgendaObserver, Boolean> observers = new ConcurrentHashMap<>(); // a map backed hashset

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

    /**
     *
     * @param tops Preorder + Name list
     */
    public Agenda(List<Pair<List<Integer>, String>> tops) {
        this("");
        Agenda ag = this;
        AtomicInteger depth = new AtomicInteger(1);
        while (tops.stream().anyMatch(p -> p.first().size() == depth.get())) {
            var ref = new Object() {
                Agenda aux = ag;
            };
            AtomicInteger auxDepth = new AtomicInteger(1);
            tops.stream().filter(p -> p.first().size() == depth.get()).sorted(new LexicographicalComparator())
                    .forEach(p -> {auxDepth.set(1); ref.aux = ag; p.first().forEach(i -> {
                        //   System.out.println("At position " + i + " maxDepth "+depth.get() + " depth" + auxDepth.get());
                        i--;
                        if (auxDepth.get() < depth.get()) {
                            ref.aux = ref.aux.getTopic(i).getSubTopics();
                        } else {
                            //   System.out.println("Added " + p.second() +" at position " + i+ "in" +ref.aux);
                            ref.aux.addTopic(new Topic(p.second(), ref.aux), i);
                        }
                        auxDepth.getAndIncrement();
                    });});
            depth.incrementAndGet();
        }
    }


    boolean removeTopic(Topic t) {
        try{
            lock.getWriteAccess();
            return this.topics.remove(t);
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            notifyObservers();
            lock.finishWrite();
        }
    }

    boolean reOrderTopic(Topic t, int pos) {
        return t.moveToNewAgenda(this, pos);
    }

    public boolean addTopic(Topic t, int pos) {
        try {
            lock.getWriteAccess();
            if (pos >= 0 && pos <= topics.size()) {
                System.out.println("Adding");
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
            notifyObservers();
            System.out.println("FInishing write");
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

    public List<String> preOrder () {
        try {
            lock.getReadAccess();
            List<String> strings  = new LinkedList<>();
            for(int i = 0; i < this.getNumberOfTopics(); i++) {
                strings.add("" + (i + 1));
                for (String s: this.topics.get(i).getSubTopics().preOrder()) {
                    strings.add((i + 1) + "." + s);
                }
            }
            return strings;
        } catch (InterruptedException ignored) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

    public int getNumberOfTopics() {
        try {
            lock.getReadAccess();
            return topics.size();
        } catch (InterruptedException e) {
            return -1;
        } finally {
            lock.finishRead();
        }
    }


    public Topic getTopicFromPreorderString(String preorder) {
        List<Integer> preorderList = getPreorderListFromPreorderString(preorder);
        try {
            lock.getReadAccess();
            return getTopicFromPreorderList(preorderList);
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public List<Integer> getPreorderListFromPreorderString(String preorder) {
        String[] preorderArray = preorder.split("\\.");
        List<Integer> preorderList = new LinkedList<>();
        for(String s : preorderArray) {
            //here we convert counting from 1 to counting from 0
            preorderList.add((Integer.parseInt(s)-1));
        }
        return preorderList;
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

    //TODO @Giuliano Add documentation to preorder methods
    //TODO @Stephan It would be great if you would look into weather / how these methods should be locked. I think you are more proficient with that.
    public Agenda getAgendaFromPreorderString(String preorder) {
        List<Integer> preorderList = getPreorderListFromPreorderString(preorder);
        try {
            lock.getReadAccess();
            return getAgendaFromPreorderList(preorderList);
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    private Agenda getAgendaFromPreorderList(List<Integer> preorder) {
        if(!preorder.isEmpty()) {
            preorder.remove(preorder.size()-1);
            if(preorder.isEmpty()) {
                return this;
            } else {
                Topic topic = getTopicFromPreorderList(preorder);
                return topic.getSubTopics();
            }
        } else {
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

    @Override
    public String toString(){
        try {
            StringBuffer sb = new StringBuffer("{\n");
            lock.getReadAccess();
            if (topics.isEmpty()) {
                return "{}";
            }
            else{
                topics.forEach(t -> sb.append(", ").append(t.toString()));
                sb.append("\n}");
                return sb.toString();
            }
        }
        catch (InterruptedException e){
            return "";
        }
        finally {
            lock.finishRead();
        }

    }
}
