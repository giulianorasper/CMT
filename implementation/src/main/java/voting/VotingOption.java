package voting;

public abstract class VotingOption {

    protected String name;
    protected int optionID;

    public int getOptionID() {
        return this.optionID;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    abstract public void addVote(int userID);

    abstract public int getCurrentResult();
}
