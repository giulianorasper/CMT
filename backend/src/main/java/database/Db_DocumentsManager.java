package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Db_DocumentsManager extends DbController implements Db_Documents_Interface {
	
	private static String table = "documents";

	public Db_DocumentsManager(String url) {
		this.url = url;
	}

	@Override
	public void addDocument(String docname, String data) {
		this.connectDB(url);
		
		String sqlstatement = "INSERT INTO documents(name,url)"
				+ "VALUES(?,?)";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setString(1, docname);
	        stmt.setString(2, data);
	        stmt.executeUpdate();
	   
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return;
	}


	@Override
	public void editDocument(String docname, int documents_id, String url) {
		this.connectDB(url);
		
		String sqlstatement = "UPDATE documents SET name = ? , "
                + "url = ? "
                + "WHERE id = ?";
 
        try {
        	PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
        	
	        stmt.setString(1, docname);
	        stmt.setString(2, url);
	        stmt.setInt(3, documents_id);
	        
	        stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnectionDB();
		return;	
	}


	@Override
	public String[] getAllDocuments() {
		this.connectDB(url);
		
		String sqlstatement = "SELECT documents_id, name, url"
				+ "FROM documents";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        ResultSet Dbtable  = stmt.executeQuery();
	   
	        while (Dbtable.next()) {
	        String name = Dbtable.getString("name");
	        String url  = Dbtable.getString("url");
	        
	        }
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();

		return null;
	}



	@Override
	public boolean existDocument(String documentname) {
		// TODO Auto-generated method stub
		String[] docs = this.getAllDocuments();
		//search Doc with right name
		return false;
	}



	@Override
	public void deleteDocument(int id) {
		this.connectDB(this.url);
		
        String sqlstatement = "DELETE FROM documents"
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
	public String getDocument(int id) {
		this.connectDB(url);
		
		String sqlstatement = "SELECT documents_id, name, url"
				+ "FROM documents"
				+ "WHERE documents_id = ?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sqlstatement); 
	        stmt.setInt(1, id);
	        ResultSet Dbtable  = stmt.executeQuery();
	   
	        String name = Dbtable.getString("name");
	        String url  = Dbtable.getString("url");
	        
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		this.closeConnectionDB();
		return null;
	}
	
	
	
}
