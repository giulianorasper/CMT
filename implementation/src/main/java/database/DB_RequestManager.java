package database;

import document.Document;
import request.*;
import user.Attendee;
import user.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_RequestManager extends DB_Controller implements DB_RequestManagement {

    private static String table = "requests";

    public DB_RequestManager(String url) {
        super(url);
    }

    @Override
    protected void init() {
        String userTable = "CREATE TABLE IF NOT EXISTS users (\n"
                + "     userID INTEGER PRIMARY KEY,\n"
                + "     fullname TEXT NOT NULL,\n"
                + "     username TEXT NOT NULL UNIQUE,\n"
                + "     password TEXT UNIQUE,\n"
                + "     token TEXT UNIQUE,\n"
                + "     email TEXT NOT NULL UNIQUE,\n"
                + "     groups TEXT NOT NULL,\n"
                + "     function TEXT NOT NULL,\n"
                + "     residence TEXT NOT NULL UNIQUE,\n"
                + "     isAdmin BOOL NOT NULL,\n"
                + "     present BOOL NOT NULL\n"
                + ") WITHOUT ROWID;";
        String requestTable = "CREATE TABLE IF NOT EXISTS requests (\n"
                + "     requestID INTEGER PRIMARY KEY,\n"
                + "     userID INTEGER NOT NULL,\n"
                + "     requestType INTEGER NOT NULL,\n"//0 for Change, 1 for Speech//TODO: Make this enum
                + "     requestableName TEXT NOT NULL,\n"
                + "     timestamps BIGINT NOT NULL,\n" //TODO: Change size to bigint
                + "     content TEXT,\n"
                + "     approved BOOL\n"
                + ") WITHOUT ROWID;";
        openConnection();
        try {
            connection.createStatement().execute(userTable);
            connection.createStatement().execute(requestTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    @Override
    public boolean addRequest(Request req) {
        this.openConnection();
        String sqlstatement = "INSERT INTO requests(requestID, userID, requestType, requestableName, timestamps," +
                "content, approved) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, req.ID);
            stmt.setInt(2, req.getRequester().getID());
            if (req instanceof ChangeRequest) { //TODO: Enum for type
                stmt.setInt(3, 0);
                stmt.setString(6, ((ChangeRequest) req).getMessage());
                stmt.setBoolean(7, ((ChangeRequest) req).isApproved());
            } else if (req instanceof SpeechRequest) {
                stmt.setInt(3, 1);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, java.sql.Types.BOOLEAN);
            } else {
                System.err.println("Requestable Type not supported by Database implementation.");
                return false;
            }
            stmt.setString(4, req.getRequestable().getName());
            stmt.setLong(5, req.getTimeStamp());
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
    public Request getRequest(int ID) {
        this.openConnection();
        Request request = null;

        String sqlstatement = "SELECT * FROM requests WHERE requestID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, ID);
            ResultSet table  = stmt.executeQuery();

            int requestID = table.getInt("requestID");
            int userID = table.getInt("userID");
            int requestType = table.getInt("requestType");
            long timestamp = table.getInt("timestamps");
            String text = table.getString("content");
            boolean approved = table.getBoolean("approved");

            String name = table.getString("requestName");
            Requestable requestable = new Requestable() {
                @Override
                public String getName() {
                    return name;
                }
            };

            String userstmt = "SELECT * FROM users WHERE userID = ? ";
            PreparedStatement user = connection.prepareStatement(userstmt);
            user.setInt(1, userID);
            ResultSet att  = user.executeQuery();
            User attendee = new Attendee(att.getString("fullname"),
                   att.getString("email"),
                   att.getString("username"),
                   att.getString("groups"),
                   att.getString("residence"),
                   att.getString("function"),
                   att.getInt("userID"));

            switch (requestType) {
                case 0: //Is ChangeRequest
                    ChangeRequest req = new ChangeRequest(requestID, attendee, requestable, timestamp, text);
                    if (approved) {
                        req.approve();
                    }
                    request = req;
                    break;
                case 1: //Is SpeechRequest
                    request = new SpeechRequest(requestID, attendee, requestable, timestamp);
                    break;
                default:
                    System.err.println("RequestType " + requestType + "found in database, which is not a" +
                            "valid request in this implementation");
                    return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return request;
    }

    @Override
    public List<Request> getAllRequests() {
        this.openConnection();
        List<Request> requests = new LinkedList<>();

        String sqlstatement = "SELECT * FROM requests";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet table  = stmt.executeQuery();

            while (table.next()) {
                int requestID = table.getInt("requestID");
                int userID = table.getInt("userID");
                int requestType = table.getInt("requestType");
                long timestamp = table.getInt("timestamps");
                String text = table.getString("content");
                boolean approved = table.getBoolean("approved");

                String name = table.getString("requestName");
                Requestable requestable = new Requestable() {
                    @Override
                    public String getName() {
                        return name;
                    }
                };

                String userstmt = "SELECT * FROM users"
                        + " WHERE userID = ? ";
                PreparedStatement user = connection.prepareStatement(userstmt);
                user.setInt(1, userID);
                ResultSet att  = user.executeQuery();
                User attendee = new Attendee(att.getString("fullname"),
                        att.getString("email"),
                        att.getString("username"),
                        att.getString("groups"),
                        att.getString("residence"),
                        att.getString("function"),
                        att.getInt("userID"));

                switch (requestType) {
                    case 0: //Is ChangeRequest
                        ChangeRequest req = new ChangeRequest(requestID, attendee, requestable, timestamp, text);
                        if (approved) {
                            req.approve();
                        }
                        requests.add(req);
                        break;
                    case 1: //Is SpeechRequest
                        requests.add(new SpeechRequest(requestID, attendee, requestable, timestamp));
                        break;
                    default:
                        System.err.println("RequestType " + requestType + "found in database, which is not a" +
                                "valid request in this implementation");
                        return null;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return requests;
    }

    @Override
    public boolean update(Request r) {
        this.openConnection();
        String sqlstatement = "UPDATE documents SET approved = ? , "
                + "requestableName = ? ,"
                + "timestamps = ?"
                + " WHERE requestID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            if (r instanceof ChangeRequest) {
                stmt.setBoolean(1, ((ChangeRequest) r).isApproved());
            } else if (r instanceof SpeechRequest) {
                stmt.setNull(1, Types.BOOLEAN);
            } else {
                System.err.println("Requestable Type not supported by Database implementation.");
                return false;
            }
            stmt.setString(2, r.getRequestable().getName());
            stmt.setLong(3, r.getTimeStamp());
            stmt.setInt(4, r.ID);
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
