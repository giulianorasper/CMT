package database;

import voting.DB_VotingManagement;
import voting.Voting;
import voting.VotingObserver;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_VotingManager extends DB_Controller implements DB_VotingManagement {

    private static String table = "votes";

    public DB_VotingManager(String url) {
        super(url);
    }

    @Override
    public boolean addVoting(Voting v) {
        return false; //TODO: Implement this
    }

    @Override
    public Voting getVoting(int ID) {
        return null; //TODO: Implement this
    }

    @Override
    public List<Voting> getVotings() {
        return null; //TODO: Implement this
    }

    @Override
    public boolean update(Voting v) {
        return false; //TODO: Implement this
    }
}
