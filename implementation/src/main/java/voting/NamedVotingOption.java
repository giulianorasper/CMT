package voting;

import java.util.ArrayList;
import java.util.List;

public class NamedVotingOption extends VotingOption {

    public List<Integer> voters = new ArrayList<>();

    /**
     * Standard constructor.
     */
    public NamedVotingOption(int ID, String name) {
        this.optionID = ID;
        this.name = name;
    }

    /**
     * Database reconstructor to remove the need to iterate again.
     * @param ID The ID of this option.
     * @param name The name of this option.
     * @param voters The userIDs of the users who voted for this option.
     */
    public NamedVotingOption(int ID, String name, List<Integer> voters) {
        this(ID, name);
        this.voters = voters;
    }

    public NamedVotingOption() {
    }

    @Override
    protected void addVote(int userID) {
        voters.add(userID);
    }

    @Override
    public int getCurrentResult() {
        return voters.size(); //TODO: Implement this
    }

    @Override
    public List<Integer> getVoters() {
        try {
            lock.getReadAccess();
            return new ArrayList<>(voters);
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }
}
