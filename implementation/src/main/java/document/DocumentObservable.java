package document;

import document.DocumentObserver;

public interface DocumentObservable {

    void register(DocumentObserver o);

    void unregister(DocumentObserver o);

    void notifyObservers();
}
