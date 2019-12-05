package voting;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface VotingManagement {

    Voting getActiveVoting();

    Voting getVoting(int ID);

    List<Voting> getVotings();


    void addVoting(Voting voting);

    void removeVoting(Voting voting);
}
