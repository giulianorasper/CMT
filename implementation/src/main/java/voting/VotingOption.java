package voting;

import com.google.gson.annotations.Expose;
import utils.WriterBiasedRWLock;

public abstract class VotingOption {

    private Voting voting;
    protected WriterBiasedRWLock lock;

    @Expose
    protected String name;
    @Expose
    protected int optionID;

    public void setParent(Voting v){
        voting = v;
        lock  = v.lock;
    }


    public int getOptionID() {
        try {
            lock.getReadAccess();
            return this.optionID;
        }
        catch (InterruptedException e){
            return -1;
        }
        finally {
            lock.finishRead();
        }
    }

    public void changeName(String newName) {
        try {
            lock.getWriteAccess();
            this.name = newName;
        }
        catch (InterruptedException e){
          //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    abstract protected void addVote(int userID);

    abstract public int getCurrentResult();

    protected void notifyObservers(){
        voting.notifyObservers();
    }
}
