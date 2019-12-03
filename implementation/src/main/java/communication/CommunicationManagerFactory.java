package communication;

import main.Conference;

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
        return new WebsocketCommunicationManager(conference, port, timeoutAfter, debugging);
    }
}
