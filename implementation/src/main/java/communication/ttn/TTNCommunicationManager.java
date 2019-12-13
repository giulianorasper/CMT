package communication.ttn;

import communication.CommunicationHandler;
import communication.CommunicationManager;
import communication.wrapper.Connection;
import communication.wrapper.TTNConnectionWrapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

@Deprecated
public class TTNCommunicationManager extends WebSocketServer implements CommunicationManager {

    private CommunicationHandler handler;
    private boolean secure;

    public TTNCommunicationManager(CommunicationHandler handler, int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Connection connection = new TTNConnectionWrapper(conn);
        handler.onMessage(connection, message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        Connection connection = new TTNConnectionWrapper(conn);
        handler.onMessage(connection, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
