package communication.wrapper;

import org.java_websocket.WebSocket;

public class TTNConnectionWrapper implements Connection {

    private WebSocket webSocket;

    public TTNConnectionWrapper(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void send(String message) {
        webSocket.send(message);
    }

    @Override
    public void close() {
        webSocket.close();
    }
}
