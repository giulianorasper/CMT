package database;

public interface Db_Documents_Interface {

    /**
    *
    * @param documentname  the documentname of the document
    * @return  true iff the document is already taken
    */
   boolean existDocument(String documentname);

   /**
    * <b>Assert:</b> documentname is not already taken<br>
    * Creates a new document if the documentname is not already taken.
    * @param docname  the name of the document
    * @param url	  the url contains the path of the file
    *
    */
   void addDocument(String docname, String url);

   /**
    * <b>Assert:</b> documentname and documents_id is already taken<br>
    * Edit a document if the documentname and the documents_id is already taken.
    * @param docname  the name of the document
    * @param documents_id  	  the id of the existing document
    * @param data	  the data contain pdf file
    *
    */
   void editDocument(String docname,int documents_id, String url);

   /**
    * <b>Assert:</b> documentname and documents_id is already taken<br>
    * Delete a document if the documentname and documents_id is already taken.
    * @param docname  		the name of the document
    * @param documents_id   the id of the existing document
    *
    */
   void deleteDocument(int id);
   /**
    *
    * @return all documents in a list of all
    */
   String[] getAllDocuments();
   
   /**
   *
   * @return the document with the right id
   */
   String getDocument(int id);
   


}
