package user;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface AttendeeManagement {

    void addAttendee(Attendee a);

    List<Attendee> getAllAttendees();

    Attendee getAttendeeData(int userID);

    void removeAttendee(int userID);

    void logoutAttendee(int userID);

    void editAttendee(Attendee attendee);

    void generateNewAttendeePassword(int userID);

    void generateNewAttendeeToken(int userID);

    void generateAllMissingAttendeePasswords(String adminToken);

    Pair<User, String> getAttendeePassword(int userID);

    List<Pair<User, String>> getAllAttendeePasswords();

    boolean logoutAllAttendees();

}
