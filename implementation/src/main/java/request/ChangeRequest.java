package request;

import user.User;

public class ChangeRequest extends Request {

    private boolean approved;
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
        this.approved = true;
        this.close();
    }

    public void disapprove(){
        this.approved = false;
        this.close();
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public void reopen() {
        this.open = false;
        this.approved = false;
    }

    /**
     * Shouldn't be called from the outside since closing without approving/disapproving is not possible
     */
    @Override
    public void close() {
        this.open = false;
    }
}
