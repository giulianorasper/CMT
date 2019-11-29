package database;

import user.Admin;
import user.DB_AdminManagement;

import java.util.List;

public class DB_AdminManager extends DB_Controller implements DB_AdminManagement {

    @Override
    public void init() {
        //TODO: Implement this
    }

    @Override
    public void openConnection() {
        //TODO: Implement this
    }

    @Override
    public void closeConnection() {
        //TODO: Implement this
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
