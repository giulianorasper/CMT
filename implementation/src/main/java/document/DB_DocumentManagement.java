package document;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_DocumentManagement {

    boolean addDocument(Document document);

    boolean deleteDocument(String name);

    boolean updateDocument(String name, String content);

    Document getDocument(String name);

    List<Document> getAllDocuments();
}
