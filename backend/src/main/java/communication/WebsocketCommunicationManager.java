package communication;

import com.google.gson.Gson;
import communication.packages.BasePacket;
import communication.packages.Packet;
import communication.packages.request.LoginRequestPacket;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class WebsocketCommunicationManager extends WebSocketServer implements CommunicationManager {

    private Gson gson = new Gson();
    //TODO make this configurable
    private static int TCP_PORT = 17699;
    private Set<WebSocket> conns;
    private CommunicationFactory factory;
    //TODO implement timeout
    private int timeoutAfter;

    public WebsocketCommunicationManager(CommunicationFactory factory, int timeoutAfter) {
        super(new InetSocketAddress(TCP_PORT));
        init(factory, timeoutAfter);
        conns = new HashSet<>();
    }

    @Override
    public void init(CommunicationFactory factory, int timeoutAfter) {
        this.factory = factory;
        this.timeoutAfter = timeoutAfter;
    }

    @Override
    public void onStart() {
        System.out.println("Starting socket server on port: " + TCP_PORT);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    //TODO connection closed during request?
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            Packet pack;
            pack = gson.fromJson(message, BasePacket.class);

            switch(pack.getPacketType()) {
                case LOGIN_REQUEST:
                    gson.fromJson(message, LoginRequestPacket.class).handle(factory, conn);
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO this should basically never happen, therefore log occurrences
        }
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
            // do some thing if required
        }
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        //TODO handle open requests and only then call stop causing the socket server to stop and close all connections
        super.stop();
    }
}
