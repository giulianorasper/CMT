package request;

import user.User;

public abstract class Request implements RequestObservable {

    protected long timeStamp;
    protected Requestable requestable;
    protected User requester;

    public final int ID;

    private boolean open;

    protected Request(int id) {
        ID = id;
    }

    public boolean isOpen() {
        return this.open;
    }

    public abstract void reopen();

    public abstract void close();

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
