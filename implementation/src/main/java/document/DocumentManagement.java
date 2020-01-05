package document;

import io.netty.buffer.ByteBuf;
import utils.OperationResponse;
import utils.Pair;

import java.io.File;
import java.util.List;

public interface DocumentManagement {

    void updateDocument(String name, String fileType, File file, boolean isCreation);

    void deleteDocument(String name);

    byte[] getDocumentContent(String name);

    Document getDocument(String name);

    List<Document> getAllDocuments();
}
