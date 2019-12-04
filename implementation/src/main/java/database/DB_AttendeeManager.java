package database;

import user.Attendee;
import user.DB_AttendeeManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_AttendeeManager extends DB_Controller implements DB_AttendeeManagement {

    public DB_AttendeeManager(String url) {
        super(url);
    }

    @Override
    public boolean addAttendee(Attendee a, String password, String token) {
        this.openConnection();
        String sqlstatement = "INSERT INTO users(userID, fullname, username, password, "
                + "token, email, groups, function, residence, isAdmin, present)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, a.getID());
            stmt.setString(2, a.getName());
            stmt.setString(3, a.getUserName());
            stmt.setString(4, password);
            stmt.setString(5, token);
            stmt.setString(6, a.getMail());
            stmt.setString(7, a.getGroup());
            stmt.setString(8, a.getFunction());
            stmt.setString(9, a.getResidence());
            stmt.setBoolean(10, false);
            stmt.setBoolean(11, false);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public List<Attendee> getAllAttendees() {
        this.openConnection();
        List<Attendee> attendees = new LinkedList<>();

        String sqlstatement = "SELECT * FROM users";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet table  = stmt.executeQuery();
            while (table.next()) {
                int ID = table.getInt("userID");
                String fullname = table.getString("fullname");
                String username = table.getString("username");
                String group = table.getString("groups");
                String function = table.getString("function");
                String residence = table.getString("residence");

                attendees.add(new Attendee(fullname, username, group, residence, function, ID));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return attendees;
    }

    @Override
    public Attendee getAttendeeData(String token) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users"
                + " WHERE token = ? ";
        Attendee attendee = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, token);
            ResultSet att  = stmt.executeQuery();
            attendee = new Attendee(att.getString("fullname"),
                    att.getString("username"),
                    att.getString("groups"),
                    att.getString("residence"),
                    att.getString("function"),
                    att.getInt("userID"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return attendee;
    }

    @Override
    public boolean editAttendee(Attendee a) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET fullname = ? , "
                + " username = ? , "
                + " groups = ? , "
                + " residence = ? , "
                + " function = ? "
                + " WHERE userID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getUserName());
            stmt.setString(3, a.getGroup());
            stmt.setString(4, a.getResidence());
            stmt.setString(5, a.getFunction());
            stmt.setInt(6, a.getID());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }
}
