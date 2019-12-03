package voting;

import com.google.gson.annotations.Expose;
import utils.OperationResponse;

import java.util.List;

public class Voting implements VotingObservable{

    @Expose
    private int ID;
    @Expose
    private String question;
    @Expose
    private List<VotingOption> options;
    @Expose
    private long openUntil;
    private VotingStatus status;

    public List<VotingOption> getOptions() {
        return null; //TODO: Implement this
    }

    public boolean addVote(int optionID, int userID) {
        //TODO: Implement this
        return true;
    }

    public boolean startVote() {
        return false; //TODO: Implement this
    }

    public boolean endVote() {
        return false; //TODO: Implement this
    }

    @Override
    public void register(VotingObserver o) {
        //TODO: Implement this
    }

    @Override
    public void unregister(VotingObserver o) {
        //TODO: Implement this
    }

    @Override
    public void notifyObservers() {
        //TODO: Implement this
    }
}
