package document;

import com.google.gson.annotations.Expose;
import request.Request;
import request.Requestable;

import java.io.File;

public class Document implements Requestable {

    //TODO private?
    public final File file;
    @Expose
    private final String name;
    @Expose
    private int revisionNumber = 1;

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

    public Integer getRevisionNumber() { return this.revisionNumber;}

    public String getPath() {return this.file.getAbsolutePath();}

    public File getFile() {
        return file;
    }
}
