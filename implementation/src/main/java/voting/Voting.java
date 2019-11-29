package voting;

import utils.OperationResponse;

import java.util.List;

public class Voting implements VotingObservable{

    private int ID;
    private String question;
    private List<VotingOption> options;
    private long openUntil;
    private VotingStatus status;

    public List<VotingOption> getOptions() {
        return null; //TODO: Implement this
    }

    public void addVote(int optionID, int userID) {
        //TODO: Implement this
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
