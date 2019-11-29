package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import login.LoginResponse;
import login.RequestValidationResponse;

public class Db_AttendeeManager extends Db_User_generalManager implements Db_Attendee_Interface {
	
	public Db_AttendeeManager(String url) {
		this.url = url;
	}


	@Override
	public boolean attendeeExists(int id) {
		boolean exist = false;
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname "
				+ "FROM participants"
				+ "WHERE participants_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setInt(1, id);
	        ResultSet Dbtable  = stmt.executeQuery();
	        
	        String firstname = Dbtable.getString("firstname");
	        if (firstname.length() > 0) {
	        	exist = true;
	        }
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return exist;
	}

	@Override
	public boolean attendeeLoggedIn(int id) {
		boolean logged_in = false;
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id,logged_in "
				+ "FROM participants"
				+ "WHERE participants_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setInt(1, id);
	        ResultSet Dbtable  = stmt.executeQuery();
	        
	        logged_in = Dbtable.getBoolean("logged_in");
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return logged_in;
	}

	@Override
	public boolean attendeeBlocked(int id, String newpw, String newtoken) {
		this.connectDB(url);
		
		String sqlstatement = "UPDATE participants SET password = ? , "
                + "token = ? "
                + "WHERE id = ?";
 
        try {
        	PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
        	
	        stmt.setString(1, newpw);
	        stmt.setString(2, newtoken);
	        stmt.setInt(3, id);
	        
	        stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnectionDB();
		return true;
	}

	
	public void addAttendee(String firstname, String lastname, String email, String party, String domicile, String password, String token) {
		this.connectDB(url);
		
		String sqlstatement = "INSERT INTO participants(firstname,lastname,"
				+ "email,password,token,party,role,domicile,logged_in) VALUES(?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setString(1, firstname);
	        stmt.setString(2, lastname);
	        stmt.setString(3, email);
	        stmt.setString(3, password);
	        stmt.setString(4, token);
	        stmt.setString(5, party);
	        stmt.setString(6, "attendee");
	        stmt.setString(7, domicile);
	        stmt.setBoolean(2, false);
	        stmt.executeUpdate();
	   
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return;
	}

	@Override
	public String[] getAllLoggedInAttendees() {
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname, lastname, email, password, token, party, role, domicile, logged_in "
				+ "FROM participants"
				+ "WHERE role = ?,"
				+ "logged_in = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
			stmt.setString(1, "attendee");
	        stmt.setBoolean(2, true);
	        ResultSet Dbtable  = stmt.executeQuery();
	        
	        while (Dbtable.next()) {
	        String firstname = Dbtable.getString("firstname");
	        String lastname  = Dbtable.getString("lastname");
	        String email = Dbtable.getString("email");
	        String party = Dbtable.getString("party");
	        String domicile = Dbtable.getString("domicile");
	        String password = Dbtable.getString("password");
	        String token = Dbtable.getString("token");
	        
	        }
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();

		return null;
	}

	@Override
	public String[] getAllAttendees() {
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname, lastname, email, password, token, party, role, domicile, logged_in "
				+ "FROM participants"
				+ "WEHRE role = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setString(1, "attendee");
	        ResultSet Dbtable  = stmt.executeQuery();
	   
	        while (Dbtable.next()) {
	        String firstname = Dbtable.getString("firstname");
	        String lastname  = Dbtable.getString("lastname");
	        String email = Dbtable.getString("email");
	        String party = Dbtable.getString("party");
	        String domicile = Dbtable.getString("domicile");
	        String password = Dbtable.getString("password");
	        String token = Dbtable.getString("token");
	        
	        }
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();

		return null;
	}


	@Override
	public String getAttendee(int id) {

		return this.getUser(id);
	}
	
	public void builddb(String url) {
		this.initDB(url);
		this.addAttendee("Franz", "Gebhard", "franz@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz1", "Gebhard", "franz1@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz2", "Gebhard", "franz2@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz3", "Gebhard", "franz3@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz4", "Gebhard", "franz4@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz5", "Gebhard", "franz5@web.de", "spd", "saarbrücken", "12345", "12345");
		this.addAttendee("Franz6", "Gebhard", "franz6@web.de", "spd", "saarbrücken", "12345", "12345");
	}

}
