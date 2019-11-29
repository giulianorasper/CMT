package database;

import document.DB_DocumentManagement;
import document.Document;
import document.DocumentObserver;

import java.util.List;

public class DB_DocumentManager extends DB_Controller implements DB_DocumentManagement, DocumentObserver {

    @Override
    public void init() {
        //TODO: Implement this
    }

    @Override
    public void openConnection() {
        //TODO: Implement this
    }

    @Override
    public void closeConnection() {
        //TODO: Implement this
    }

    @Override
    public boolean addDocument(Document document) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean deleteDocument(String name) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean updateDocument(String name, String content) {
        return false; //TODO: Implement this
    }

    @Override
    public Document getDocument(String name) {
        return null; //TODO: Implement this
    }

    @Override
    public List<Document> getAllDocuments() {
        return null; //TODO: Implement this
    }

    @Override
    public boolean update(Document r) {
        return false; //TODO: Implement this
    }
}
