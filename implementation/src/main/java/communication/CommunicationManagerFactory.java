package communication;

import main.Conference;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * A factory for creating an {@link CommunicationManager} for a {@link Conference} handling incoming requests from the frontend.
 */
public class CommunicationManagerFactory {

    Conference conference;
    int port;
    int timeoutAfter;
    boolean debugging;

    /**
     * Initialized the {@link CommunicationManagerFactory} using a not guaranteed to
     * @param conference the conference to handle communication for
     */
    public CommunicationManagerFactory(Conference conference) {
        this.conference = conference;
        this.port = 17699;
        this.timeoutAfter = 10;
        this.debugging = false;
    }

    /**
     *
     * @param port the port to listen on for incoming requests
     * @return this
     */
    public CommunicationManagerFactory setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     *
     * @param timeoutAfter the number of seconds a request is allowed to wait for a response before the connection gets forcefully closed
     * @return this
     */
    public CommunicationManagerFactory setTimeoutAfter(int timeoutAfter) {
        this.timeoutAfter = timeoutAfter;
        return this;
    }

    /**
     * Enables debugging functionaries such as fancy printing for JSON and more detailed logs
     * @return this
     */
    public CommunicationManagerFactory enableDebugging() {
        this.debugging = true;
        return this;
    }

    /**
     *
     * @return A communication manager produced using the given information
     */
    public CommunicationManager create() {
        WebsocketCommunicationManager manager = new WebsocketCommunicationManager(conference, port, timeoutAfter, debugging);
        try {
            SSLContext context = getContext();
            if( context == null ) throw new IllegalArgumentException();
            manager.setWebSocketFactory( new DefaultSSLWebSocketServerFactory( getContext() ) );
        } catch (Exception e) {
            if(debugging) {
                System.out.println("[]----------[SECURITY ALERT]----------[]");
                System.out.println("No certificate found. Sever will be started in not encrypted debugging mode!");
                e.printStackTrace();
            } else {
                System.out.println("Could not retrieve certificate. The server can not be started for security reasons.");
                System.exit(0);
            }
        }
        return manager;
    }

    private static SSLContext getContext() {
        SSLContext context;
        String password = "CHANGEIT";
        String pathname = "pem";
        try {
            context = SSLContext.getInstance( "TLS" );

            byte[] certBytes = parseDERFromPEM( getBytes( new File( pathname + File.separator + "cert.pem" ) ), "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----" );
            byte[] keyBytes = parseDERFromPEM( getBytes( new File( pathname + File.separator + "privkey.pem" ) ), "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----" );

            X509Certificate cert = generateCertificateFromDER( certBytes );
            RSAPrivateKey key = generatePrivateKeyFromDER( keyBytes );

            KeyStore keystore = KeyStore.getInstance( "JKS" );
            keystore.load( null );
            keystore.setCertificateEntry( "cert-alias", cert );
            keystore.setKeyEntry( "key-alias", key, password.toCharArray(), new Certificate[]{ cert } );

            KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
            kmf.init( keystore, password.toCharArray() );

            KeyManager[] km = kmf.getKeyManagers();

            context.init( km, null, null );
        } catch ( Exception e ) {
            context = null;
        }
        return context;
    }

    private static byte[] parseDERFromPEM( byte[] pem, String beginDelimiter, String endDelimiter ) {
        String data = new String( pem );
        String[] tokens = data.split( beginDelimiter );
        tokens = tokens[1].split( endDelimiter );
        return Base64.getDecoder().decode( tokens[0] );
    }

    private static RSAPrivateKey generatePrivateKeyFromDER( byte[] keyBytes ) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( keyBytes );

        KeyFactory factory = KeyFactory.getInstance( "RSA" );

        return ( RSAPrivateKey ) factory.generatePrivate( spec );
    }

    private static X509Certificate generateCertificateFromDER( byte[] certBytes ) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance( "X.509" );

        return ( X509Certificate ) factory.generateCertificate( new ByteArrayInputStream( certBytes ) );
    }

    private static byte[] getBytes( File file ) {
        byte[] bytesArray = new byte[( int ) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream( file );
            fis.read( bytesArray ); //read file into bytes[]
            fis.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return bytesArray;
    }
}
