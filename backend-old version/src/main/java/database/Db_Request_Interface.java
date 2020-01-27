package database;


/**
 * Interface that enables the communication between the database and the Request_Change functionality. 
 **/

public interface Db_Request_Interface {

    /**
    *
    * @param requestname  the requestname of the request
    * @return  true iff the request is already taken
    */
   boolean requestExists(String requestname);

   /**
    * <b>Assert:</b> requestname is not already taken<br>
    * Creates a new request if the requestname is not already taken.
    * @param requestname  the name of the requestument
    * @param data	  the data 
    *
    */
   void addRequest(String requestname, String data);

   /**
    * <b>Assert:</b> requestname and request_id is already taken<br>
    * Edit a request if the requestname and the request_id is already taken.
    * @param requestname  the name of the request
    * @param request_id  	  the id of the existing request
    * @param data	  the data contain pdf file
    *
    */
   void editRequest(String requestname,int request_id, String data);

   /**
    * <b>Assert:</b> requestname and request_id is already taken<br>
    * Delete a request if the requestname and request_id is already taken.
    * @param requestname  		the name of the request
    * @param request_id   the id of the existing request
    *
    */
   void deleteRequest(String docname,int id);
   /**
    *
    * @return all request in a list of all
    */
   String[] getAllRequests();


	
}
