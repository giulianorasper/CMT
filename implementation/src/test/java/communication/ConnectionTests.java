package communication;

import communication.packets.request.LoginRequestPacket;
import main.Conference;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.Attendee;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionTests {

    private static AtomicInteger portCounter = new AtomicInteger(10000);
    private int port;
    private CommunicationManager communicationManager;
    private Conference conference;

    public void before() {
        port = portCounter.getAndIncrement();
        conference = new Conference(true);
        Attendee attendee = new Attendee("a", "a", "a", "a", "a", "a");
        conference.addAttendee(attendee, "login");
        CommunicationManagerFactory factory = new CommunicationManagerFactory(conference).setPort(port).ignoreSecurityAlerts();
        communicationManager = factory.create();
        communicationManager.start();
    }

    @After
    public void after() {
        communicationManager.stop();
    }

    @Test(timeout = 60000)
    public void stressTest() throws Exception {
        int amount = 500 ;
        WebSocketClient[] clients = new WebSocketClient[amount];
        for(int i = 0; i < amount; i++) {
            clients[i] = new WebSocketClient(port);
            clients[i].start();
            Assert.assertTrue(clients[i].isRunning());
            Assert.assertTrue(clients[i].isConnected());
        }
        for(int i = 0; i < amount; i++) {
            WebSocketClient client = clients[i];
            LoginRequestPacket packet = new LoginRequestPacket("a", "login");
            client.send(packet);
        }
        for(int i = 0; i < amount; i++) {
            WebSocketClient client = clients[i];
            client.getChannel().closeFuture().sync();
            Assert.assertTrue("Client " + i + " did not reveive ananser.", client.isSuccessful());
        }
    }

    @Test(timeout = 15000)
    public void clientTimeout() throws Exception {
        WebSocketClient client = new WebSocketClient(port);
        long start = System.currentTimeMillis();
        client.start();
        client.getChannel().closeFuture().sync();
        long end = System.currentTimeMillis();
        long time = end - start;
        client.stop();
        Assert.assertTrue(time > 10000 && time < 12000);
    }
}
