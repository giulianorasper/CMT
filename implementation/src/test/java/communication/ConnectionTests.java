package communication;

import communication.packets.request.LoginRequestPacket;
import main.Conference;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionTests {

    private static AtomicInteger portCounter = new AtomicInteger(10000);
    private int port;
    private CommunicationManager communicationManager;
    private Conference conference;

    @Before
    public void before() {
        port = portCounter.getAndIncrement();
        conference = new Conference(true);
        CommunicationManagerFactory factory = new CommunicationManagerFactory(conference).enableDebugging().setPort(port).ignoreSecurityAlerts();
        communicationManager = factory.create();
        communicationManager.start();
    }

    @After
    public void after() {
        communicationManager.stop();
    }

    @Test
    public void connect() throws Exception {
        int amount = 1;
        WebSocketClient[] clients = new WebSocketClient[amount];
        for(int i = 0; i < amount; i++) {
            clients[i] = new WebSocketClient(port);
            clients[i].start();
            Assert.assertTrue(clients[i].isRunning());
            Assert.assertTrue(clients[i].isConnected());
        }
        for(int i = 0; i < amount; i++) {
            WebSocketClient client = clients[i];
            LoginRequestPacket packet = new LoginRequestPacket("dummy", "dummy");
            client.send(packet, (g) -> {
                //client.getChannel().close();
            });
        }
        for(int i = 0; i < amount; i++) {
            WebSocketClient client = clients[i];
            client.getChannel().closeFuture().sync();
            client.stop();
        }
    }

    public void testTimeout() throws Exception {
        WebSocketClient client = new WebSocketClient(port);
        long start = System.currentTimeMillis();
        client.start();
        client.getChannel().closeFuture().sync();
        long end = System.currentTimeMillis();
        long time = end - start;
        Assert.assertTrue(time > 10000 && time < 12000);
    }
}
