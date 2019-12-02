package database;

import document.DB_DocumentManagement;
import document.Document;
import document.DocumentObserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_DocumentManager extends DB_Controller implements DB_DocumentManagement, DocumentObserver {

    private static String table = "documents";

    public DB_DocumentManager(String url) {
        super(url);
    }

    @Override
    public boolean addDocument(Document document) {
        this.openConnection();
        String sqlstatement = "INSERT INTO documents(name,url)"
                + "VALUES(?,?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, document.name);
            stmt.setString(2, "content");//TODO: Read Content out of Document file
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        this.closeConnection();
        return false; //TODO: Return correct response
    }

    @Override
    public boolean deleteDocument(String name) {
        this.openConnection();
        //TODO: Delete by name, documents do not even have IDs
        String sqlstatement = "DELETE FROM documents"
                + " WHERE id = ?";

        try  {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            //stmt.setInt(1, id);//TODO: Change this
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnection();
        return false; //TODO: Return correct response
    }

    @Override
    public boolean updateDocument(String name, String content) {
        this.openConnection();
        //TODO: Change by name, documents do not have IDs
        String sqlstatement = "UPDATE documents SET name = ? , "
                + "url = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);

            stmt.setString(1, name);
            stmt.setString(2, url);
            //stmt.setInt(3, documents_id);//TODO: Change this

            stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.closeConnection();
        return false; //TODO: Return correct response
    }

    @Override
    public Document getDocument(String name) {
        this.openConnection();
        //TODO: Search by name, documents do not have IDs
        String sqlstatement = "SELECT documents_id, name, url"
                + "FROM documents"
                + "WHERE documents_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            //stmt.setInt(1, id);//TODO: Change this
            ResultSet Dbtable  = stmt.executeQuery();

            //String name = Dbtable.getString("name");//TODO: This is never used
            //String url  = Dbtable.getString("url");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        this.closeConnection();
        return null; //TODO: Return concrete Document object
    }

    @Override
    public List<Document> getAllDocuments() {
        this.openConnection();

        String sqlstatement = "SELECT documents_id, name, url"
                + "FROM documents";

        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet Dbtable  = stmt.executeQuery();

            while (Dbtable.next()) {
                String name = Dbtable.getString("name");//TODO: This is never used
                String url  = Dbtable.getString("url");

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        this.closeConnection();

        return null;//TODO: Return the concrete Document objects
    }

    @Override
    public boolean update(Document r) {
        return false; //TODO: Implement this
    }
}
