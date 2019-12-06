import communication.CommunicationManager;
import communication.CommunicationManagerFactory;
import main.Conference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TestServer {

    /*@Test
   public void testing() throws InterruptedException{
        CommunicationManager w = new CommunicationManagerFactory(new Conference()).enableDebugging().create();
        w.start();
        Thread.sleep(100000000);
   }
*/
    @Before
    public void cleanup() {
        URI path = Paths.get("testdb/testdb.db").toUri();
        new File(path).delete();
    }
}
