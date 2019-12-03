package voting;

public class AnonymousVotingOption extends VotingOption {

    private int votes;

    @Override
    protected void addVote(int userID) {
        votes++;
    }

    @Override
    public int getCurrentResult() {
        return votes; //TODO: Implement this
    }
}
