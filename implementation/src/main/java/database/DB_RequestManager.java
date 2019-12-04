package database;

import request.ChangeRequest;
import request.DB_RequestManagement;
import request.Request;
import request.RequestObserver;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_RequestManager extends DB_Controller implements DB_RequestManagement {

    private static String table = "requests";

    public DB_RequestManager(String url) {
        super(url);
    }

    @Override
    public boolean addRequest(Request req) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean closeRequest(Request req) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean reopenRequest(Request req) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean approveRequest(ChangeRequest req) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean disapproveRequest(ChangeRequest req) {
        return false; //TODO: Implement this
    }

    @Override
    public Request getRequest(int ID) {
        return null; //TODO: Implement this
    }

    @Override
    public List<Request> getAllRequests() {
        return null; //TODO: Implement this
    }

    @Override
    public boolean update(Request r) {
        return false; //TODO: Implement this
    }
}
