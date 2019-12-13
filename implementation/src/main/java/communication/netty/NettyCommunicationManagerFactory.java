package communication.netty;

import communication.CommunicationHandler;
import communication.CommunicationManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.io.File;

public class NettyCommunicationManagerFactory {

    CommunicationHandler handler;
    int port;
    File cert;
    File key;

    public NettyCommunicationManagerFactory(CommunicationHandler handler, int port, File cert, File key) {
        this.handler = handler;
        this.port = port;
        this.cert = cert;
        this.key = key;
    }

    public CommunicationManager create() {
        SslContext sslContext = null;
        try {
            sslContext = SslContextBuilder.forServer(cert, key).build();
        } catch (Exception e) {

        }
        NettyCommunicationManager manager;
        if(sslContext == null) {
            manager = new NettyCommunicationManager(handler, port);
        } else {
            manager = new NettyCommunicationManager(handler, port, sslContext);
        }
        return manager;
    }
}
