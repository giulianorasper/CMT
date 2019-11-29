package database;

import java.sql.*;

/**
 * Create the database for a conference and communicate with it.
 */

public abstract class DbController {
	
	
public Connection connection;
public String url;

/*
 * establish connection to the DB and create DB.
 *
 */
public void connectDB(String URL) {
    try {
    	if (connection == null) {
            System.out.println("A new database has been created.");
        }
        // create a connection to the database
    	//String url = System.getProperty("user.home") + "/" + "DBname";
        connection = DriverManager.getConnection(URL);
        System.out.println("Connection to SQLite has been established.");
        
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

/*
 * close connection to the db.
 */

public void closeConnectionDB() {
	try {
        if (connection != null) {
            connection.close();
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
}

public void initDB(String url) {
    String participantstable = "CREATE TABLE IF NOT EXISTS participants (\n"
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
    String requesttable = "CREATE TABLE IF NOT EXISTS requests (\n"
            + "    request_id integer PRIMARY KEY,\n"
            + "    participants_id integer NOT NULL,\n"
            + "    change_of_request BOOL NOT NULL,\n"
            + "    topic text NOT NULL,\n"
            + "    timestamp text NOT NULL,\n"
            + "    message text,\n"
            + "    close BOOL\n"
            + ") [WITHOUT ROWID];";
    String agendatable = "CREATE TABLE IF NOT EXISTS agenda (\n"
            + "    topic_id integer PRIMARY KEY,\n"
            + "    topicname text NOT NULL\n"
            + "    parent_id integer NOT NULL,\n"
            + "    order integer NOT NULL,\n"
            + ") [WITHOUT ROWID];";
    String documentstable = "CREATE TABLE IF NOT EXISTS documents (\n"
            + "    documents_id integer PRIMARY KEY,\n"
            + "    name text NOT NULL,\n"
            + "    url text NOT NULL\n"
            + ") [WITHOUT ROWID];";
    String votetable = "CREATE TABLE IF NOT EXISTS votes1 (\n"
            + "    option_id integer NOT NULL,\n"
            + "    option text NOT NULL,\n"
            + "    participants_id text NOT NULL Unique,\n"
            + ");";
    
    connectDB(url);
	try {
		Statement stmt0 = connection.createStatement();
        // create participantstable
        stmt0.execute(participantstable);
        Statement stmt1 = connection.createStatement();
        // create requesttable
        stmt1.execute(requesttable);
        Statement stmt2 = connection.createStatement();
        // create Agendatable
        stmt2.execute(agendatable);
        Statement stmt4 = connection.createStatement();
        // create documentstable
        stmt4.execute(documentstable);
        Statement stmt3 = connection.createStatement();
        // create Votetable
        stmt3.execute(votetable);
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	} 
	closeConnectionDB();   
}

public void insertIntoDB(String sqlstatement) {
	this.connectDB(url);
	try {
		PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
        stmt.setString(1, "kjhl");
        stmt.setInt(2, 10);
        stmt.executeUpdate();
   
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
}


}