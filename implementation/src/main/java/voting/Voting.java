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

public class Voting implements VotingObservable {

    @Expose
    private final int ID;
    @Expose
    private boolean namedVote;
    @Expose
    private String question;
    @Expose
    private List<VotingOption> options;
    @Expose
    private long openUntil;
    @Expose
    private VotingStatus status;
    //the duration of the voting in seconds
    @Expose
    private int duration;
    private static int lastUsedID = 0;
    private static Lock idLock = new ReentrantLock();
    protected WriterBiasedRWLock lock = new WriterBiasedRWLock();
    private ConcurrentHashMap<VotingObserver, Boolean> observers = new ConcurrentHashMap<>(); // a map backed hashset
    public List<Integer> voters = new ArrayList<>();

    /**
     * Constructor Voting before the voting started
     *
     * @param options  A list of VotingOptions with their results.
     * @param question The question of the voting.
     */
    public Voting(List<VotingOption> options, String question, boolean namedVote) {
        this.options = options;
        this.question = question;
        this.namedVote = namedVote;
        ID = getNextId();
        status = VotingStatus.Created;
        options.forEach(o -> o.setParent(this));
    }

    /**
     * Constructor for the Database to easily reconstruct Voting Results in case the voting has ended.
     *
     * @param options  A list of VotingOptions with their results.
     * @param question The question of the voting.
     * @param ID       The ID of the voting that was already stored in the Database.
     */
    public Voting(List<VotingOption> options, String question, int ID, boolean namedVote) {
        this.options = options;
        this.question = question;
        this.ID = ID;
        this.namedVote = namedVote;
        status = VotingStatus.Closed;
        this.options.forEach(o -> o.setParent(this));
    }

    /**
     * Check if the current voting is a NamedVoting
     * @return true iff the voting is a NamedVoting
     */
    public boolean isNamedVote() {
        return namedVote;
    }

    /**
     * Get the ID of the voting
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Get the Question of the voting
     * @return Question
     */
    public String getQuestion() {
        try {
            lock.getReadAccess();
            return question;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

    /**
     * Get Value how long the user can vote
     * @return OpenUntil
     */
    public long getOpenUntil() {
        try {
            lock.getReadAccess();
            return openUntil;
        } catch (InterruptedException e) {
            return -1;
        } finally {
            lock.finishRead();
        }
    }

    /**
     * Get Status of the voting. For example: Created, Running, Close...
     * @return status
     */
    public VotingStatus getStatus() {
        try {
            lock.getReadAccess();
            return status;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

    /**
     * Get all Voting Options.
     * @return List of VotingOptions
     */
    public List<VotingOption> getOptions() {
        try {
            lock.getReadAccess();
            return new ArrayList<>(options);
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

    /**
     * Add to the VotingOption the userID from User that vote for the VotingOption
     * @param optionID VotingOption
     * @param userID from User
     * @return true iff voting was accepted
     */
    public boolean addVote(int optionID, int userID) {
        try {
            lock.getWriteAccess();
            if (voters.contains(userID) || status != VotingStatus.Running || options.size() <= optionID || optionID < 0) {
                return false;
            }
            voters.add(userID);
            options.get(optionID).addVote(userID);
            return true;
        } catch (InterruptedException e) {
            return false;
        } finally {
            lock.finishWrite();
        }
    }

    /**
     * Set Duration how long user can vote.
     * @param seconds duration
     */
    public void setDuration(int seconds) {
        try {
            lock.getWriteAccess();
            this.duration = seconds;
        } catch (InterruptedException e) {

        } finally {
            lock.finishWrite();
        }
    }

    /**
     * Start an created Vote. So the User can vote for a specific duration for this vote.
     * @return true iff the vote has started
     */
    public boolean startVote() {
        try {
            lock.getWriteAccess();
            if (status != VotingStatus.Created)
                throw new IllegalArgumentException("Votes which are already running or ended can not be started.");
            openUntil = System.currentTimeMillis() + duration * 1000;
            status = VotingStatus.Running;
            notifyObservers();
            return true;
        } catch (InterruptedException e) {
            return false;
        } finally {
            lock.finishWrite();
        }
    }

    /**
     * End a running Vote. So the user can´t vote anymore.
     * @return
     */
    public boolean endVote() {
        try {
            lock.getWriteAccess();
            status = VotingStatus.Closed;
            for (VotingOption votingOption : options) {
                votingOption.publishVotes();
            }
            notifyObservers();
            return true;
        } catch (InterruptedException e) {
            return false;
        } finally {
            lock.finishWrite();
        }
    }

    /**
     * Calculate next free ID
     * @return ID
     */
    private static int getNextId() {
        try {
            idLock.lock();
            lastUsedID++;
            return lastUsedID;
        } finally {
            idLock.unlock();
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
        for (VotingObserver vo : observers.keySet()) {
            vo.update(this);
        }
    }


}
