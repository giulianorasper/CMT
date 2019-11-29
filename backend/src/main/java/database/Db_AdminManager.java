package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import login.LoginResponse;
import login.RequestValidationResponse;

public class Db_AdminManager extends Db_User_generalManager implements Db_Admin_Interface {

	public Db_AdminManager(String url) {
		this.url = url;
	}

	public boolean adminExists(int id) {
		boolean exist = false;
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, role, firstname "
				+ "FROM participants"
				+ "WHERE role = ?,"
				+ "participants_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
			stmt.setString(1, "admin");
	        stmt.setInt(2, id);
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

	public boolean adminLoggedIn(int id) {
		boolean logged_in = false;
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, role, logged_in "
				+ "FROM participants"
				+ "WHERE  role = ?,"
				+ "participants_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
			stmt.setString(1, "admin");
	        stmt.setInt(2, id);
	        ResultSet Dbtable  = stmt.executeQuery();
	        
	        logged_in = Dbtable.getBoolean("logged_in");
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return logged_in;
	}

	public boolean adminBlocked(int id, String newpw, String newtoken) {
		this.connectDB(url);
		
		String sqlstatement = "UPDATE participants SET password = ? , "
                + "token = ? "
                + "WHERE role = ?,"
                + "id = ?";
 
        try {
        	PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
        	
	        stmt.setString(1, newpw);
	        stmt.setString(2, newtoken);
	        stmt.setString(3, "admin");
	        stmt.setInt(4, id);
	        
	        stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnectionDB();
		return true;
	}

	public void addAdmin(String firstname, String lastname, String email, String party, String domicile, String password, String token) {
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
	        stmt.setString(6, "admin");
	        stmt.setString(7, domicile);
	        stmt.setBoolean(2, false);
	        stmt.executeUpdate();
	   
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return;
	}

	public String[] getAllAdmins() {
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname, lastname, email, password, token, party, role, domicile, logged_in "
				+ "FROM participants"
				+ "WEHRE role = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setString(1, "admin");
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


	public String[] getAllLoggedInAdmins() {
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname, lastname, email, password, token, party, role, domicile, logged_in "
				+ "FROM participants"
				+ "WHERE role = ?,"
				+ "logged_in = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
			stmt.setString(1, "admin");
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
	public String[] getAdmin(int id) {
		this.connectDB(url);
		
		String sqlstatement = "SELECT participants_id, firstname, lastname, email, password, token, party, role, domicile, logged_in "
				+ "FROM participants"
				+ "WHERE participants_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setInt(1, id);
	        ResultSet Dbtable  = stmt.executeQuery();
	   
	        String firstname = Dbtable.getString("firstname");
	        String lastname  = Dbtable.getString("lastname");
	        String email = Dbtable.getString("email");
	        String party = Dbtable.getString("party");
	        String domicile = Dbtable.getString("domicile");
	        String password = Dbtable.getString("password");
	        String token = Dbtable.getString("token");
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();

		return null;
	}


}
