package document;

import request.Request;
import request.Requestable;

import java.io.File;

public class Document implements Requestable, DocumentObservable {

    private File file;
    private String name;
    private int revisionNumber;

    public void rename(String name) {
        //TODO: Implement this
    }

    public void updateFile(File file) {
        //TODO: Implement this
    }

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
