package database;

public interface Db_Vote_Interface {

    /**
    *
    * @param votename  the votename of the vote
    * @return  true iff the vote is already taken
    */
   boolean voteExists(String votename);

   /**
    * <b>Assert:</b> votename is not already taken<br>
    * Creates a new vote if the votename is not already taken.
    * @param votename  the name of the vote
    * @param data	  the data 
    *
    */
   void addVote(String votename, String data);

   /**
    * <b>Assert:</b> votename and vote_id is already taken<br>
    * Edit a vote if the votename and the vote_id is already taken.
    * @param votename  the name of the vote
    * @param vote_id  	  the id of the existing vote
    * @param data	  the data 
    *
    */
   void editVote(String votename,int vote_id, String data);

   /**
    * <b>Assert:</b> votename and vote_id is already taken<br>
    * Delete a vote if the votename and vote_id is already taken.
    * @param votename  		the name of the vote
    * @param vote_id   the id of the existing vote
    *
    */
   void deleteVote(String votename,int id);
   /**
    *
    * @return all vote in a list of all
    */
   String[] getAllVote();


}
