package voting;

import java.util.ArrayList;
import java.util.List;

public class NamedVotingOption extends VotingOption {

    public List<Integer> voters = new ArrayList<>();

    @Override
    protected void addVote(int userID) {
        voters.add(userID);
    }

    @Override
    public int getCurrentResult() {
        return voters.size(); //TODO: Implement this
    }
}
