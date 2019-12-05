package document;

import request.Request;
import request.Requestable;

import java.io.File;

public class Document implements Requestable {

    public final File file;
    private final String name;
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
}
