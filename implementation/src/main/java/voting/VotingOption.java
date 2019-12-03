package voting;

import com.google.gson.annotations.Expose;

public abstract class VotingOption {

    @Expose
    protected String name;
    @Expose
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
