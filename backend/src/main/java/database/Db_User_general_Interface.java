package database;

import login.*;

/**
 * Interface that enables the communication between the database and the login functionality. For a description on how the login works see {@link login}
 */
public interface Db_User_general_Interface {

    /**
    *
    * @param id  the id provided by the user
    * @return  User Object
    */
   String getUser(int id);

    /**
     *
     * @param username  the username provided by the attendee
     * @param password  not to be confused with a token
     * @return  {@link LoginResponse}
     */
    LoginResponse checkLogin(int id, String password);

    /**
     * <b>Assert:</b> the username must exist
     * @param username  the username of the attendee
     * @return   the login tokens which is valid for this user.
     */
    String getToken(int id);

    /**
     *
     * @param username - the username provided by the attendee
     * @param password - not to be confused with a token
     * @return - {@link RequestValidationResponse}
     */
    RequestValidationResponse checkToken(int id, String password);

    /** Logs the user out by invalidating the token and all passwords. Generates a new token and password, but does not destroy the old ones
     * @param username  the username of the attendee
     */
    void logout(int id, String newpw, String newtoken);

    /** Logs the user out by removing all login data of the user
     * @param username the username of the attendee
     */
    void deleteLoginData(int id);

    /**
     * Generates a new password for the user. Logging in with the new password and the username should lead to the same token as all other valid passwords
     * @param username  the username of the attendee
     * @param password  changes the password of the attendee to a new one
     * @return true - iff the password could be changed (i.e. the password is different from the previous one)
     *
     */
    boolean changePassword(int id, String password);

    /**
     *
     * @param username  the username of the attendee
     * @return A password which has not been invalidated jet. If there is no such password this method must return null
     */
    String getPassword(int id);

}
