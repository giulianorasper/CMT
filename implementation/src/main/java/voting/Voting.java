package voting;

import com.google.gson.annotations.Expose;
import agenda.AgendaObserver;
import utils.OperationResponse;
import utils.WriterBiasedRWLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Voting implements VotingObservable{

    @Expose
    private final int ID;

    public boolean isNamedVote() {
        return namedVote;
    }

    //TODO handle setting this boolean
    @Expose
    private boolean namedVote;
    private ConcurrentHashMap<VotingObserver, Boolean> observers = new ConcurrentHashMap<>(); // a map backed hashset
    public List<Integer> voters = new ArrayList<>();
    public int getID() {
        return ID;
    }

    public String getQuestion() {
        try {
            lock.getReadAccess();
            return question;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public long getOpenUntil() {
        try {
            lock.getReadAccess();
            return openUntil;
        }
        catch (InterruptedException e){
            return -1;
        }
        finally {
            lock.finishRead();
        }
    }

    public VotingStatus getStatus() {
        try {
            lock.getReadAccess();
            return status;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    @Expose
    private String question;

    @Expose
    private List<VotingOption> options;

    @Expose
    private long openUntil;
    private VotingStatus status;

    private static int lastUsedID=0;
    private static Lock idLock = new ReentrantLock();
    protected WriterBiasedRWLock lock = new WriterBiasedRWLock();

    public Voting(List<VotingOption> options, String question){
        this.options = options;
        this.question = question;
        ID = getNextId();
        status = VotingStatus.Created;
        options.forEach(o -> o.setParent(this));
    }

    /**
     * Constructor for the Database to easily reconstruct Voting Results in case the voting has ended.
     * @param options A list of VotingOptions with their results.
     * @param question The question of the voting.
     * @param ID The ID of the voting that was already stored in the Database.
     */
    public Voting(List<VotingOption> options, String question, int ID) {
        this.options = options;
        this.question = question;
        this.ID = ID;
        status = VotingStatus.Closed;
        this.options.forEach(o -> o.setParent(this));
    }

    public List<VotingOption> getOptions() {
        try{
            lock.getReadAccess();
            return new ArrayList<>(options);
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public boolean addVote(int optionID, int userID) {
        try{
            lock.getWriteAccess();
            if(voters.contains(userID) || status!= VotingStatus.Running || options.size() <= optionID || optionID < 0){
                return false;
            }
            voters.add(userID);
            options.get(optionID).addVote(userID);
            return true;
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    public boolean startVote() {
        try{
            lock.getWriteAccess();
            status = VotingStatus.Running;
            return true;
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    public boolean endVote() {
        try{
            lock.getWriteAccess();
            status = VotingStatus.Closed;
            notifyObservers();
            return true;
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishWrite();
        }
    }

    @Override
    public void register(VotingObserver o) {
        observers.put(o, true);
    }

    @Override
    public void unregister(VotingObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (VotingObserver vo: observers.keySet()) {
            vo.update(this);
        }
    }

    private static int getNextId(){
        try{
            idLock.lock();
            lastUsedID++;
            return lastUsedID;
        }finally {
            idLock.unlock();
        }
    }
}
