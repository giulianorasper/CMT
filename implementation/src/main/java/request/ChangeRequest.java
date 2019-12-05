package request;

import com.google.gson.annotations.Expose;
import user.User;

public class ChangeRequest extends Request {

    @Expose
    private boolean approved;
    @Expose
    private String message;

    public ChangeRequest(User requester, Requestable topic, long timestamp, String message){
        super(topic, requester, timestamp);
        this.message = message;

        this.approved = false;
    }

    public ChangeRequest(int id, User requester, Requestable topic, long timestamp, String message) {
        super(id, topic, requester, timestamp);
        this.message = message;
        this.approved = false;
    }


    public void approve(){
        try {
            lock.getWriteAccess();
            this.approved = true;
            this.open = false;
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            lock.finishWrite();
        }
    }

    @Override
    public void reopen() {
        try {
            lock.getWriteAccess();
            this.open = true;
            this.approved = false;
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            lock.finishWrite();
        }
    }

    public void disapprove(){
        try {
            lock.getWriteAccess();
            this.approved = false;
            this.open = false;
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            lock.finishWrite();
        }
    }

    public String getMessage(){
        return this.message;
    }


}
