package communication.packets.response;

import com.google.gson.annotations.Expose;
import communication.enums.PacketType;
import communication.enums.RequestResult;
import communication.packets.ResponsePacket;
import document.Document;

import java.util.List;

public class GetDocumentListResponsePacket extends ResponsePacket {

    @Expose
    private List<Document> documents;

    public GetDocumentListResponsePacket(List<Document> documents) {
        super(PacketType.GET_DOCUMENT_LIST_RESPONSE, RequestResult.Valid);
        this.documents = documents;
    }
}
