package user;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_AttendeeManagement {

    boolean addAttendee(Attendee a);

    List<Attendee> getAllAttendees();

    Attendee getAttendeeData(String token);

    boolean editAttendee(Attendee a);
}
