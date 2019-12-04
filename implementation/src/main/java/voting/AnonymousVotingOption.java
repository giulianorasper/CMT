package voting;

import java.util.List;

public class AnonymousVotingOption extends VotingOption {

    private int votes;

    /**
     * Database reconstructor to remove the need to add the votes all the time.
     * @param votes The amount of votes this option has got.
     */
    public AnonymousVotingOption(int votes) {
        this.votes = votes;
    }

    @Override
    protected void addVote(int userID) {
        votes++;
    }

    @Override
    public int getCurrentResult() {
        return votes; //TODO: Implement this
    }

    @Override
    public List<Integer> getVoters() {
        return null;
    }
}
