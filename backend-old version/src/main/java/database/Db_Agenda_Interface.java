package database;

public interface Db_Agenda_Interface {

    /**
    *
    * @param topicname  the topicname of the document
    * @return  true iff the topic is already taken
    */
   boolean existTopic(String topicname);

   /**
    * <b>Assert:</b> topicname is not already taken<br>
    * Creates a new topic if the topicname is not already taken.
    * @param topicname  the name of the topic
    * @param sequence	the secence of the topic like 1.1.1 or 1.2
    *
    */
   void addTopic(String topicname, String sequence);

   /**
    * <b>Assert:</b> old_topicname and topic_id is already taken<br>
    * rename a topic if the old_topicname and the topic_id is already taken.
    * @param old_topicname  the old name of the topic
    * @param topic_id  	    the id of the topic
    * @param new_name	 	the new topic name
    *
    */
   void renameTopic(String old_topicname,int topic_id, String new_name);

   /**
    * <b>Assert:</b> topicname and old_topic_id is already taken<br>
    * Change sequence of a topic if the topicname and the old_topic_id is already taken.
    * @param topicname  the old name of the topic
    * @param old_topic_id  	    the id of the topic
    * @param new_id	 	the new sequence for the topic
    *
    */
   void changeTopic(String topicname,int old_topic_id, String new_id);
   
   /**
    * <b>Assert:</b> topicname and topic_id is already taken<br>
    * Delete a topic if the topicname and topic_id is already taken.
    * @param topicname 		the name of the topic
    * @param topic_id   	the id of the existing topic
    *
    */
   void deleteTopic(String topicname,int topic_id);
   /**
    *
    * @return all topics in a list of all
    */
   String[] getAllTopics();

}
