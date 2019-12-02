package request;

import user.User;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Request implements RequestObservable {

    protected static int lastUsedID = 0;

    //Locking static variable lastUsedID for multiple admins
    private static Lock idLock = new ReentrantLock();

    protected long timeStamp;
    protected Requestable requestable;
    protected User requester;

    public final int ID;

    protected boolean open;

    protected Request(int id, Requestable topic, User requester, long timestamp){
        try {
            idLock.lock();

            if (id > lastUsedID) {
                lastUsedID = id;
            }

            this.ID = id;
            this.requestable = topic;
            this.requester = requester;
            this.timeStamp = timestamp;

            this.open = true;

        } finally {
            idLock.unlock();
        }
    }

    protected Request(Requestable topic, User requester, long timestamp){
        this(getNextID(), topic, requester, timestamp);
    }

    public boolean isOpen() {
        return this.open;
    }

    public abstract void reopen();

    public abstract void close();

    protected static int getNextID(){
        try{
            idLock.lock();
            lastUsedID++;
            return lastUsedID;
        }finally {
            idLock.unlock();
        }
    }

    public Requestable getTopic(){
        return this.requestable;
    }

    @Override
    public void register(RequestObserver o) {
        //TODO: Implement this
    }

    @Override
    public void unregister(RequestObserver o) {
        //TODO: Implement this
    }

    @Override
    public void notifyObservers() {
        //TODO: Implement this
    }
}
