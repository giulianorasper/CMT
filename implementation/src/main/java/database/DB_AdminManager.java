package database;

import user.Admin;
import user.DB_AdminManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_AdminManager extends DB_Controller implements DB_AdminManagement {

    public DB_AdminManager(String url) {
        super(url);
    }

    @Override
    public boolean addAdmin(Admin a, String password, String token) {
        this.openConnection();
        String sqlstatement = "INSERT INTO users(userID, fullname, username, password ,"
            + "token, email, groups, function, residence, isAdmin, present)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, a.getID());
            stmt.setString(2, a.getName());
            stmt.setString(3, a.getUserName());
            stmt.setString(4, password);
            stmt.setString(5, token);
            stmt.setString(6, a.getMail());
            stmt.setString(7, a.getGroup());
            stmt.setString(8, a.getFunction());
            stmt.setString(9, a.getResidence());
            stmt.setBoolean(10, true);
            stmt.setBoolean(11, false);
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
    public List<Admin> getAllAdmins() {
        this.openConnection();
        List<Admin> admins = new LinkedList<>();

        String sqlstatement = "SELECT * FROM users WHERE isAdmin = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setBoolean(1, true);
            ResultSet table  = stmt.executeQuery();
            while (table.next()) {
                int ID = table.getInt("userID");
                String fullname = table.getString("fullname");
                String userName = table.getString("username");
                String group = table.getString("groups");
                String function = table.getString("function");
                String residence = table.getString("residence");

                admins.add(new Admin(fullname, userName, group, residence, function, ID));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return admins;
    }

    @Override
    public Admin getAdminData(String adminToken) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users"
                + " WHERE token = ? AND isAdmin = ?";
        Admin admin = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, adminToken);
            stmt.setBoolean(2, true);
            ResultSet adm  = stmt.executeQuery();
            admin = new Admin(adm.getString("fullname"),
                    adm.getString("username"),
                    adm.getString("groups"),
                    adm.getString("residence"),
                    adm.getString("function"),
                    adm.getInt("userID"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return admin;
    }

    @Override
    public boolean editAdmin(Admin a) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET fullname = ? , "
                + "groups = ? , "
                + "residence = ? , "
                + "function = ?"
                + " WHERE userID = ? AND isAdmin = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getGroup());
            stmt.setString(3, a.getResidence());
            stmt.setString(4, a.getFunction());
            stmt.setInt(5, a.getID());
            stmt.setBoolean(6, true);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }
}
