package request;

import user.User;

public class SpeechRequest extends Request {

    public SpeechRequest(User requester, Requestable topic, long timestamp) {
        super(topic, requester, timestamp);
    }

    public SpeechRequest(int id, User requester, Requestable topic, long timestamp) {
        super(id, topic, requester, timestamp);
    }

    @Override
    public void reopen() {
        try {
            lock.getWriteAccess();
            this.open = true;
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            lock.finishWrite();
        }
    }


    public void close() {
        try {
            lock.getWriteAccess();
            this.open = false;
        } catch (InterruptedException e) {
            //do nothing
        } finally {
            lock.finishWrite();
        }
    }
}
