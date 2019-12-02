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

    protected Request(int id, Requestable topic){
        try {
            idLock.lock();

            if (id > lastUsedID) {
                lastUsedID = id;
            }

            this.ID = id;
            this.requestable = topic;

        } finally {
            idLock.unlock();
        }
    }

    protected Request(Requestable topic){
        this.ID = getNextID();
        this.requestable = topic;
    }

    public boolean isOpen() {
        return this.open;
    }

    public abstract void reopen();

    public abstract void close();

    protected int getNextID(){
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
