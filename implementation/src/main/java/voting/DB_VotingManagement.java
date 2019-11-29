package voting;

import java.util.List;

public interface DB_VotingManagement {

    boolean addVoting(Voting v);

    Voting getVoting(int ID);

    List<Voting> getVotings();
}
