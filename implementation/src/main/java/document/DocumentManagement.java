package document;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface DocumentManagement {

    void updateDocument(String name, String fileType, byte[] fileBytes, boolean isCreation);

    void deleteDocument(String name);

    byte[] getDocumentContent(String name);

    Document getDocument(String name);

    List<Document> getAllDocuments();
}
