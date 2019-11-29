package database;

import java.sql.*;

/**
 * Create the database for a conference and communicate with it.
 */
public class DB_Controller {

    public Connection connection;
    public String url;

    public DB_Controller(String url) {
        this.url = url;
    }

    public void init(){
        //TODO: Change this table, we never require anything that works on only parts of the name,
        // so we store this in a single column
        //TODO: Domicile should be called residence to be consistent with the rest of the project
        String participantsTable = "CREATE TABLE IF NOT EXISTS participants (\n"
                + "    participants_id integer PRIMARY KEY,\n"
                + "    firstname text NOT NULL,\n"
                + "    lastname text NOT NULL,\n"
                + "    email text NOT NULL UNIQUE,\n"
                + "    password text NOT NULL UNIQUE,\n"
                + "    token text NOT NULL UNIQUE,\n"
                + "    party text NOT NULL,\n"
                + "    role text NOT NULL,\n"
                + "    domicile text NOT NULL UNIQUE,\n"
                + "    logged_in BOOL NOT NULL\n"
                + ") [WITHOUT ROWID];";
        String requestTable = "CREATE TABLE IF NOT EXISTS requests (\n"
                + "    request_id integer PRIMARY KEY,\n"
                + "    participants_id integer NOT NULL,\n"
                + "    change_of_request BOOL NOT NULL,\n"
                + "    topic text NOT NULL,\n"
                + "    timestamp text NOT NULL,\n"
                + "    message text,\n"
                + "    close BOOL\n"
                + ") [WITHOUT ROWID];";
        String agendaTable = "CREATE TABLE IF NOT EXISTS agenda (\n"
                + "    topic_id integer PRIMARY KEY,\n"
                + "    topicname text NOT NULL\n"
                + "    parent_id integer NOT NULL,\n"
                + "    order integer NOT NULL,\n"
                + ") [WITHOUT ROWID];";
        String documentsTable = "CREATE TABLE IF NOT EXISTS documents (\n"
                + "    documents_id integer PRIMARY KEY,\n"
                + "    name text NOT NULL,\n"
                + "    url text NOT NULL\n"
                + ") [WITHOUT ROWID];";
        String voteTable = "CREATE TABLE IF NOT EXISTS votes1 (\n"
                + "    option_id integer NOT NULL,\n"
                + "    option text NOT NULL,\n"
                + "    participants_id text NOT NULL Unique,\n"
                + ");";

        openConnection();
        try {
            Statement stmt0 = connection.createStatement();
            stmt0.execute(participantsTable);
            Statement stmt1 = connection.createStatement();
            stmt1.execute(requestTable);
            Statement stmt2 = connection.createStatement();
            stmt2.execute(agendaTable);
            Statement stmt4 = connection.createStatement();
            stmt4.execute(documentsTable);
            Statement stmt3 = connection.createStatement();
            stmt3.execute(voteTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
    };

    public void openConnection() {
        try {
            if (connection == null) {
                System.out.println("A new database has been created.");
            }
            connection = DriverManager.getConnection(this.url);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    };

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    };
}
