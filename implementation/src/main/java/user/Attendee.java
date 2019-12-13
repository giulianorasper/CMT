package user;

import com.google.gson.annotations.Expose;

public class Attendee extends User {

    private int numberOfDevices;
    @Expose
    private boolean present;

    public Attendee(String name, String email, String userName, String group, String residence, String function, int ID){
        super(name, email, userName, group, function, residence, ID);

        this.numberOfDevices = 0;
        this.present = false;
    }

    public Attendee(String name, String email, String userName, String group, String residence, String function) {
        this(name, email, userName,group,residence,function,  nextFreeId());
    }

    public void additionalDevice(){
        try {
            lock.getWriteAccess();
            numberOfDevices++;
        }
            catch (InterruptedException e){
            //do nothing
        }
            finally {
            lock.finishWrite();
        }
    }

    public void attendedConference(){
        try {
            lock.getWriteAccess();
            this.present = true;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public void logout(){
        this.present = false;
    }

    public boolean isPresent() {
        try {
            lock.getReadAccess();
            return present;
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishRead();
        }
    }

    public int getNumberOfDevices() {
        try {
            lock.getReadAccess();
            return numberOfDevices;
        }
        catch (InterruptedException e){
            return -1;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setPresent(boolean present) {
        try {
            lock.getWriteAccess();
            this.present = present;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }
}
