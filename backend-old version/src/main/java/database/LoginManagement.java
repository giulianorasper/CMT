package database;

import login.*;

/**
 * Interface that enables the communication between the database and the login functionality. For a description on how the login works see {@link login}
 */
public interface LoginManagement {

    /**
     *
     * @param username  the username provided by the attendee
     * @param password  not to be confused with a token
     * @return  {@link LoginResponse}
     */
    LoginResponse checkLogin(String username, String password);

    /**
     * <b>Assert:</b> the username must exist
     * @param username  the username of the attendee
     * @return   the login tokens which is valid for this user.
     */
    String getToken(String username);

    /**
     *
     * @param username - the username provided by the attendee
     * @param password - not to be confused with a token
     * @return - {@link RequestValidationResponse}
     */
    RequestValidationResponse checkToken(String username, String password);

    /** Logs the user out by invalidating the token and all passwords. Generates a new token and password, but does not destroy the old ones
     * @param username  the username of the attendee
     */
    void logout(String username);

    /** Logs the user out by removing all login data of the user
     * @param username the username of the attendee
     */
    void deleteLoginData(String username);

    /**
     * Generates a new password for the user. Logging in with the new password and the username should lead to the same token as all other valid passwords
     * @param username  the username of the attendee
     */
    void generateNewPassword(String username);

    /**
     *
     * @param username  the username of the attendee
     * @return A password which has not been invalidated jet
     */
    String getUnusedPassword(String username);

    /**
     *
     * @param username  the username of the attendee
     * @return  true iff the username is already taken
     */
    boolean userExists(String username);

    /**
     * <b>Assert:</b> username is not already taken<br>
     * Creates a new user if the username is not already taken. After creation the token and a password should be available
     * @param username  the username of the attendee
     *
     */
    void addUser(String username);
}
