package voting;

import java.util.List;

public class AnonymousVotingOption extends VotingOption {

    private int votes;

    /**
     * Standard constructor.
     */
    public AnonymousVotingOption(int ID, String name) {
        this.optionID = ID;
        this.name = name;
    }

    /**
     * Database reconstructor to remove the need to add the votes all the time.
     * @param ID The ID of this option.
     * @param name The name of this option.
     * @param votes The amount of votes this option has got.
     */
    public AnonymousVotingOption(int ID, String name, int votes) {
        this(ID, name);
        this.votes = votes;
    }


    /**
     * increase Number of user that vote for the NamedVotingOption.
     * @param userID
     */
    @Override
    protected void addVote(int userID) {
        votes++;
    }

    @Override
    public int getCurrentResult(){
        return votes;
        /*try {
            lock.getReadAccess();
            return votes;
        }
            catch (InterruptedException e){
            return -1;
        }
            finally {
            lock.finishRead();
        }*/
    }

    /**
     * AnonymousVote cant get userids
     * return Null
     */
    @Override
    public List<Integer> getVoters() {
        return null;
    }

    /**
     * Get Number of User that vote for the NamedVotingOption.
     * return Number of User
     */
    @Override
    protected void publishVotes() {
        setPublicVotes(votes);
    }
}
