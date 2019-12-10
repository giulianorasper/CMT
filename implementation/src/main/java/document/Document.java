package document;

import com.google.gson.annotations.Expose;
import request.Request;
import request.Requestable;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Document implements Requestable {

    //TODO private?
    private final File file;
    @Expose
    private final String name;
    @Expose
    private int revisionNumber = 1;

    private Lock lock = new ReentrantLock();

    public Document(String path, String name) {
        file = new File(path);
        this.name = name;
    }

    public Document(String path, String name, int revisionNumber) {
        this(path, name);
        this.revisionNumber = revisionNumber;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Integer getRevisionNumber() {
        try {
            lock.lock();
            return this.revisionNumber;
        }
        finally {
            lock.unlock();
        }

    }

    public String getPath() {return this.file.getAbsolutePath();}

    public File getFile() {
        return file;
    }

    public void incrementRevision(){
        lock.lock();
        revisionNumber++;
        lock.unlock();
    }
}
