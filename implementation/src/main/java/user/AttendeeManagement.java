package user;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface AttendeeManagement {

    OperationResponse addAttendee(String adminToken, Attendee a);

    Pair<OperationResponse, List<Attendee>> getAllAttendees(String adminToken);

    Pair<OperationResponse, Attendee> getAttendeeData(String userToken);

    OperationResponse removeAttendee(String adminToken, int userID);

    OperationResponse logoutAttendee(String adminToken, int userID);

    OperationResponse editAttendee(String adminToken, int userID, Attendee attendee);

    OperationResponse generateNewAttendeePassword(String adminToken, int userID);

    OperationResponse generateNewAttendeeToken(String adminToken, int userID);

    OperationResponse generateAllMissingAttendeePasswords(String adminToken);

    Pair<OperationResponse, Pair<User, String>> getAttendeePassword(String adminToken, int userID);

    Pair<OperationResponse, List<Pair<User, String>>> getAllAttendeePasswords(String adminToken);

    boolean logoutAllAttendees();

    OperationResponse logoutAttendees(String adminToken);
}
