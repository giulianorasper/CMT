package document;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface DocumentManagement {

    void addDocument(String name, String content);

    void deleteDocument(String name);

    void updateDocument(String name, String content);

    Document getDocument(String name);

    List<Document> getAllDocuments();
}
