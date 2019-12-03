import communication.CommunicationManager;
import communication.CommunicationManagerFactory;
import java.util.Scanner;

public class TestServer {

   public static void main(String[] args) {
        CommunicationManager w = new CommunicationManagerFactory(null).enableDebugging().create();
        w.start();
       System.out.println("Type anything to stop the serer.");
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        try {
            w.stop();
        } catch (Exception e) {
            e.printStackTrace();
       }
   }
}
