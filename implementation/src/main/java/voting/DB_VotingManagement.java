package voting;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_VotingManagement {

    boolean addVoting(Voting v);

    Voting getVoting(int ID);

    List<Voting> getVotings();
}
