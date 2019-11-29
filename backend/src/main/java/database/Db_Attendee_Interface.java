package database;

public interface Db_Attendee_Interface extends Db_User_general_Interface {

    /**
    *
    * @param username  the username of the attendee
    * @return  true iff the username is already taken
    */
   boolean attendeeExists(int id);

   /**
    *
    * @param username  the username of the attendee
    * @return  true iff the user has logged in
    */
   boolean attendeeLoggedIn(int id);

   /**
    *
    * @param username  the username of the attendee
    * @return  true iff the user has not been blocked by the administration (for example if they announce that they
    * will not be able to attend)
    */
   boolean attendeeBlocked(int id, String newpw, String newtoken);

   /**
    * <b>Assert:</b> username is not already taken<br>
    * Creates a new user if the username is not already taken. After creation the token and a password should be available
    * @param username  the username of the attendee
    * @param password  the initial password
    * @param token the token of this user
    *
    */
   void addAttendee(String firstname, String lastname, String email, String party, String domicile, String password, String token);

   /**
    *
    * @return the user names of all users
    */
   String[] getAllAttendees();

   /**
   *
   * @return the user names of all users
   */
  String getAttendee(int id);

   /**
    *
    * @return the user names of all logged in users
    */
   String[] getAllLoggedInAttendees();

   
}

