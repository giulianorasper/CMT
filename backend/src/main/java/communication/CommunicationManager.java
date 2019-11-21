package communication;

//TODO Add thrown exceptions e.g. NotInitialized

import java.io.IOException;

/**
 * The interface that is responsible with waiting for requests and processing them. Receives a {@link CommunicationFactory} which it uses to process requests
 */
public interface CommunicationManager {

    /**
     * Initializes the Manager (equivalently to a class constructor)
     * @param factory - the communication factory used to answer requests
     * @param timeoutAfter - the number of seconds a request is allowed to wait for a response before the connection gets forcefully closed
     */
    void init(CommunicationFactory factory, int timeoutAfter);

    /**
     * Waits for requests and generates responses using the functionality provided by the {@link CommunicationFactory}.
     * Should close the connection if completing the requests takes to long in order to prevent slow loris attacks.
     * What exactly to long means is specified by the init function
     */
    void start();

    /**
     * After calling this function the CommunicationManager stops processing new requests.
     */
    void stop() throws IOException, InterruptedException;

}
