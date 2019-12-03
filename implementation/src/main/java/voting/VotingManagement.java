package voting;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface VotingManagement {

    Pair<OperationResponse, Voting> getActiveVoting(String token);

    Pair<OperationResponse, Voting> getVoting(String token, int ID);

    Pair<OperationResponse, List<Voting>> getVotings(String token);

    OperationResponse addVoting(String token, Voting voting);

    OperationResponse removeVoting(String token, Voting voting);
}
