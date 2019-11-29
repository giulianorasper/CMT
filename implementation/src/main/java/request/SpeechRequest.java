package request;

import user.User;

public class SpeechRequest extends Request {

    public SpeechRequest(User requester, long timestamp) {
        super();
        this.requester = requester;
        this.timeStamp = timestamp;

        this.open = true;
    }

    public SpeechRequest(int id, User requester, long timestamp) {
        super(id);
        this.requester = requester;
        this.timeStamp = timestamp;

        this.open = true;
    }

    @Override
    public void reopen() {
        this.open = true;
    }

    @Override
    public void close() {
        this.open = false;
    }
}
