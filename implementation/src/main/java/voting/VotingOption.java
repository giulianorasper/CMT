package voting;

import com.google.gson.annotations.Expose;
import utils.WriterBiasedRWLock;

import java.util.List;

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

    public String getName() {
        try {
            lock.getReadAccess();
            return this.name;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

    public void setOptionID(int newID) {
        try {
            lock.getWriteAccess();
            this.optionID = newID;
        } catch (InterruptedException e) {

        } finally {
            lock.finishWrite();
        }
    }

    abstract protected void addVote(int userID);

    abstract public int getCurrentResult();

    abstract public List<Integer> getVoters();

    protected void notifyObservers(){
        voting.notifyObservers();
    }
}
