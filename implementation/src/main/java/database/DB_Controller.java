package database;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.*;

import static java.lang.System.exit;

/**
 * Create the database for a conference and communicate with it.
 */
@SuppressWarnings("checkstyle:typename")
public class DB_Controller {

    public Connection connection;
    public String url;

    public DB_Controller(String url) {
        URI path = Paths.get(url).toUri();
        File file = new File(path);
        if (!file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        this.url = "jdbc:sqlite:" + url;
    }

    public void init(){
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
                + "     requestType INTEGER NOT NULL,\n"
                + "     requestableName TEXT NOT NULL,\n"
                + "     timestamps INTEGER NOT NULL,\n" //TODO: Change size to bigint
                + "     content TEXT,\n"
                + "     approved BOOL\n"
                + ") WITHOUT ROWID;";
        String agendaTable = "CREATE TABLE IF NOT EXISTS agenda (\n"
                + "     topicPosition TEXT NOT NULL,\n"
                + "     topicName TEXT NOT NULL\n"
                + ");";
        String documentTable = "CREATE TABLE IF NOT EXISTS documents (\n"
                + "     path TEXT NOT NULL,\n"
                + "     documentName TEXT NOT NULL UNIQUE,\n"
                + "     revision INTEGER NOT NULL\n"
                + ");";

        openConnection();
        try {
            connection.createStatement().execute(userTable);
            connection.createStatement().execute(requestTable);
            connection.createStatement().execute(agendaTable);
            connection.createStatement().execute(documentTable);
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
            exit(e.getErrorCode());//TODO: ???
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
