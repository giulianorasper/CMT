package database;

import user.Attendee;
import user.DB_AttendeeManagement;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_AttendeeManager extends DB_Controller implements DB_AttendeeManagement {

    public DB_AttendeeManager(String url) {
        super(url);
    }

    @Override
    public boolean addAttendee(Attendee a) {
        return false; //TODO: Implement this
    }

    @Override
    public List<Attendee> getAllAttendees() {
        return null; //TODO: Implement this
    }

    @Override
    public Attendee getAttendeeData(String token) {
        return null; //TODO: Implement this
    }

    @Override
    public boolean editAttendee(Attendee a) {
        return false; //TODO: Implement this
    }
}
