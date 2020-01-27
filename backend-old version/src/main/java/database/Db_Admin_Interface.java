package database;

public interface Db_Admin_Interface extends Db_User_general_Interface {

    /**
    *
    * @param username  the username of the attendee
    * @return  true iff the username is already taken
    */
   boolean adminExists(int id);

   /**
    *
    * @param username  the username of the attendee
    * @return  true iff the user has logged in
    */
   boolean adminLoggedIn(int id);

   /**
    *
    * @param username  the username of the attendee
    * @return  true iff the user has not been blocked by the administration (for example if they announce that they
    * will not be able to attend)
    */
   boolean adminBlocked(int id, String newpw, String newtoken);

   /**
    * <b>Assert:</b> username is not already taken<br>
    * Creates a new user if the username is not already taken. After creation the token and a password should be available
    * @param username  the username of the attendee
    * @param password  the initial password
    * @param token the token of this user
    *
    */
   void addAdmin(String firstname, String lastname, String email, String party, String domicile, String password, String token);

   /**
    *
    * @return the user names of all users
    */
   String[] getAllAdmins();

   /**
    *
    * @return the user names of all logged in users
    */
   String[] getAllLoggedInAdmins();
   
   /**
   *
   * @return the user names of all users
   */
  String[] getAdmin(int id);


}
