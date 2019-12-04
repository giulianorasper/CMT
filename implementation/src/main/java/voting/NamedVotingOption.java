package voting;

import java.util.ArrayList;
import java.util.List;

public class NamedVotingOption extends VotingOption {

    public List<Integer> voters = new ArrayList<>();

    /**
     * Database reconstructor to remove the need to iterate again.
     * @param voters The userIDs of the users who voted for this option.
     */
    public NamedVotingOption(List<Integer> voters) {
        this.voters = voters;
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
            return voters;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }
}
