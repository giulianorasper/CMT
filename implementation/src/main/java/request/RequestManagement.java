package request;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface RequestManagement {

    OperationResponse addRequest(String adminToken, Request request);

    Pair<OperationResponse, Request> getRequest(String adminToken, int ID);

    Pair<OperationResponse, List<Request>> getAllRequests(String adminToken);
}
