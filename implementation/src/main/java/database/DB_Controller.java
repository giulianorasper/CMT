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
public abstract class DB_Controller {

    protected Connection connection;
    protected String url;

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
            System.err.println("Encountered an exception while trying to find the database." +
                    "Please make sure the path is correct.");
            e.printStackTrace();
            exit(1);
        }
        this.url = "jdbc:sqlite:" + url;
        init();

    }

    /**
     * Initializes the neccessary tables for the database
     */
    protected abstract void init();

    public void openConnection() {
        try {
            if (connection == null) {
                System.out.println("A new database has been created.");
            }
            connection = DriverManager.getConnection(this.url);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException ex) {
            System.err.println("An exception occured in the database connection.");
            System.err.println(ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("An exception occured in the database connection.");
            System.err.println(ex.getMessage());
        }
    }
}
