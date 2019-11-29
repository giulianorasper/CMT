package database;

import request.ChangeRequest;
import request.DB_RequestManagement;
import request.Request;
import request.RequestObserver;

import java.util.List;

public class DB_RequestManager extends DB_Controller implements DB_RequestManagement, RequestObserver {

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
