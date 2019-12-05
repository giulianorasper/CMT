package database;

import document.DB_DocumentManagement;
import document.Document;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_DocumentManager extends DB_Controller implements DB_DocumentManagement {

    private static String table = "documents";

    public DB_DocumentManager(String url) {
        super(url);
    }

    @Override
    protected void init() {
        String documentTable = "CREATE TABLE IF NOT EXISTS documents (\n"
                + "     path TEXT NOT NULL,\n"
                + "     documentName TEXT NOT NULL UNIQUE,\n"
                + "     revision INTEGER NOT NULL\n"
                + ");";
        openConnection();
        try {
            connection.createStatement().execute(documentTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    @Override
    public boolean addDocument(Document document) {
        this.openConnection();
        String sqlstatement = "INSERT INTO documents(path, documentName, revision)"
                + "VALUES(?,?,?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, document.file.getAbsolutePath());
            stmt.setString(2, document.getName());
            stmt.setInt(3, 1);
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
    public boolean deleteDocument(String name) {
        this.openConnection();
        String sqlstatement = "DELETE FROM documents"
                + " WHERE documentName = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public boolean updateDocument(String oldName, String newName) {
        this.openConnection();
        String revisionNumber = "SELECT revision FROM documents"
                + " WHERE documentName = ?";
        String sqlstatement = "UPDATE documents SET revision = ? , "
                + "documentName = ?"
                + " WHERE documentName = ?";
        try {
            PreparedStatement rev = connection.prepareStatement(revisionNumber);
            rev.setString(1, oldName);
            ResultSet res = rev.executeQuery();

            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, res.getInt("revision") + 1);
            stmt.setString(2, newName);
            stmt.setString(3, oldName);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public Document getDocument(String name) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM documents"
                + " WHERE documentName = ?";
        Document document = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, name);
            ResultSet doc  = stmt.executeQuery();
            document = new Document(doc.getString("path"),
                    doc.getString("documentName"),
                    doc.getInt("revision"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return document;
    }

    @Override
    public List<Document> getAllDocuments() {
        this.openConnection();
        List<Document> documents = new LinkedList<>();

        String sqlstatement = "SELECT * FROM documents";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet table  = stmt.executeQuery();

            while (table.next()) {
                String name = table.getString("documentName");
                String url  = table.getString("path");
                int revision = table.getInt("revision");
                documents.add(new Document(url, name, revision));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return documents;
    }
}
