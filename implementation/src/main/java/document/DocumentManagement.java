package document;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface DocumentManagement {

    OperationResponse addDocument(String adminToken, String name, String content);

    OperationResponse deleteDocument(String adminToken, String name);

    OperationResponse updateDocument(String adminToken, String name, String content);

    Pair<OperationResponse, Document> getDocument(String token, String name);

    Pair<OperationResponse, List<Document>> getAllDocuments(String token);
}
