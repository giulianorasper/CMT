import communication.WebsocketCommunicationManager;
import java.util.Scanner;

public class TestServer {

   public static void main(String[] args) {
        WebsocketCommunicationManager w = new WebsocketCommunicationManager(null,0);
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
