package voting;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class NamedVotingOption extends VotingOption {

    @Expose
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

    /**
     * Add user with userID to the list of user that vote for the NamedVotingOption.
     * @param userID
     */
    @Override
    protected void addVote(int userID) {
        voters.add(userID);
    }


    /**
     * Get Number of User that vote for the NamedVotingOption.
     * return Number of User or -1
     */
    @Override
    public int getCurrentResult() {
        try {
            lock.getReadAccess();
            return voters.size();
        }
        catch (InterruptedException e){
            return -1;
        }
        finally {
            lock.finishRead();
        }
    }

    /**
     * Get UserIDs of User that vote for the NamedVotingOption.
     * @return a list of the user ids of the voters
     */
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

    /**
     * Publish the Number of User that vote for the NamedVotingOption.
     */
    @Override
    protected void publishVotes() {
        setPublicVotes(voters.size());
    }
}
