package request;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_RequestManagement extends RequestObserver{

    boolean addRequest(Request req);

    Request getRequest(int ID);

    List<Request> getAllRequests();
}
