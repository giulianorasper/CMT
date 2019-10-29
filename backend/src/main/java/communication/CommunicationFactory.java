package communication;


import login.LoginResponse;
import login.RequestValidationResponse;
import utils.Pair;

import java.nio.file.Path;
import java.util.function.Function;

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
     * @return - A pair consisting of a {@link LoginResponse} and the token. If the request is not valid the token will be
     * null.
     */
    Pair<LoginResponse, String> checkLogin(String username, String password);

}
