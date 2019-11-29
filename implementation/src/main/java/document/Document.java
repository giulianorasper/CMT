package document;

import request.Request;
import request.Requestable;

import java.io.File;

public class Document implements Requestable, DocumentObservable {

    public File file;
    public String name;
    private int revisionNumber;

    @Override
    public void register(DocumentObserver o) {
        //TODO: Implement this
    }

    @Override
    public void unregister(DocumentObserver o) {
        //TODO: Implement this
    }

    @Override
    public void notifyObservers() {
        //TODO: Implement this
    }
}
