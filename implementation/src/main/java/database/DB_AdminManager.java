package database;

import user.Admin;
import user.DB_AdminManagement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DB_AdminManager extends DB_Controller implements DB_AdminManagement {

    public DB_AdminManager(String url) {
        super(url);
    }

    @Override
    public boolean addAdmin(Admin a, String password) {
        return false; //TODO: Implement this
    }

    @Override
    public List<Admin> getAllAdmins() {
        return null; //TODO: Implement this
    }

    @Override
    public Admin getAdminData(String adminToken) {
        return null; //TODO: Implement this
    }

    @Override
    public boolean editAdmin(Admin a) {
        return false; //TODO: Implement this
    }
}
