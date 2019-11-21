package communication;


import login.LoginResponse;
import utils.Pair;

import java.nio.file.Path;

/**
 * Abstracts all functions of the communication between the java backend and the client.
 * Those functions will be implemented by the server instance. The communication part of the project is suposed to
 * call them (not to implement them)
 */
public interface CommunicationFactory {

    /**
     * A function that the communication system can use to get the path of the html document containing the main website
     * @return - the path of the main HTML document the user needs
     */
    Path getWebsitePath();

    /**
     * A function that the communication system can use to check if a login is valid
     * @param username - the username provided by the request
     * @param password - the password provided by the request
     * @return - A pair consisting of a {@link LoginResponse}, a token, and the unix timestamp in millis specifying
     * when the token should expire.
     * If the {@link LoginResponse} is not Valid then the second argument will be null
     */
    Pair<LoginResponse, Pair<String,Long>> checkLogin(String username, String password);

}
