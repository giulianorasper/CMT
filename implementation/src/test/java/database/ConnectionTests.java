package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class ConnectionTests {

    private URI path;

    @Before
    public void init() throws IOException {
        path = Paths.get("db/database.db").toUri();
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    @After
    public void cleanup() {
        new File(path).delete();
    }

    @Test
    public void initializeDatabase() {
        DB_Controller controller = new DB_Controller("db/database.db");
        controller.init();
    }
}
