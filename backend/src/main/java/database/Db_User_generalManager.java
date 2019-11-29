package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import login.LoginResponse;
import login.RequestValidationResponse;

public abstract class Db_User_generalManager extends DbController implements Db_User_general_Interface  {

	
	@Override
	public String getUser(int id) {
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

	
	@Override
	public LoginResponse checkLogin(int id, String password) {
		  String user = this.getUser(id);
		  // if user.password = password -> valid
		  // if user.password != password -> Wrong password
		  // if user.password = "" -> userdoesnotexist
		  // if user.logged_in -> pw alreadyused
		return null;
	}

	@Override
	public String getToken(int id) {
		String user = this.getUser(id);
		//user.token
		return null;
	}

	@Override
	public RequestValidationResponse checkToken(int id, String password) {
		// TODO Auto-generated method stub
		//password = token ??
		// equal to checklogin ?
		return null;
	}

	@Override
	public void logout(int id, String newpw, String newtoken) {
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
		return;
	}

	@Override
	public void deleteLoginData(int id) {
		this.connectDB(this.url);
		
        String sqlstatement = "DELETE FROM participants"
        		+ " WHERE id = ?";
        
        try  {
        	PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
            stmt.setInt(1, id);
            stmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnectionDB();
        return;
    }
	

	@Override
	public boolean changePassword(int id, String password) {
		this.connectDB(url);
		
		String sqlstatement = "UPDATE participants SET password = ? "
                + "WHERE id = ?";
 
        try {
        	PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
        	
	        stmt.setString(1, password);
	        stmt.setInt(2, id);
	        
	        stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnectionDB();
		return true;
	}

	@Override
	public String getPassword(int id) {
		String user = this.getUser(id);
		//user.password
		return null;
	}

}
