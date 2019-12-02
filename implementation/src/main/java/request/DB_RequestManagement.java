package request;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_RequestManagement {

    boolean addRequest(Request req);

    boolean closeRequest(Request req);

    boolean reopenRequest(Request req);

    boolean approveRequest(ChangeRequest req);

    boolean disapproveRequest(ChangeRequest req);

    Request getRequest(int ID);

    List<Request> getAllRequests();
}
