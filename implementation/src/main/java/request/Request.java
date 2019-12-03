package request;

import user.User;
import utils.WriterBiasedRWLock;
import voting.VotingObserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Request implements RequestObservable {

    protected static int lastUsedID = 0;
    private ConcurrentHashMap<RequestObserver, Boolean> observers = new ConcurrentHashMap<>(); // a map backed hashset


    //Locking static variable lastUsedID for multiple admins
    private static Lock idLock = new ReentrantLock();

    protected WriterBiasedRWLock lock = new WriterBiasedRWLock();

    protected long timeStamp;


    public abstract void reopen();

    public Requestable getRequestable() {
        try {
            lock.getReadAccess();
            return requestable;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public User getRequester() {
        try {
            lock.getReadAccess();
            return this.requester;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.finishRead();
        }
    }

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
        try {
            lock.getReadAccess();
            return this.open;
        } catch (InterruptedException e) {
            return false;
        } finally {
            lock.finishRead();
        }
    }


    protected static int getNextID(){
        try{
            idLock.lock();
            lastUsedID++;
            return lastUsedID;
        }finally {
            idLock.unlock();
        }
    }


    @Override
    public void register(RequestObserver o) {
        observers.put(o, true);
    }

    @Override
    public void unregister(RequestObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEachKey(2, o -> o.update(this));
    }
}
