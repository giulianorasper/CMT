package database;

import user.DB_GeneralUserManagement;
import user.LoginResponse;
import user.TokenResponse;
import user.User;
import utils.Pair;

import java.util.List;

public class DB_GeneralUserManager extends DB_Controller implements DB_GeneralUserManagement {

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
    public Pair<LoginResponse, String> checkLogin(String name, String password) {
        return null; //TODO: Implement this
    }

    @Override
    public TokenResponse checkToken(String token) {
        return null; //TODO: Implement this
    }

    @Override
    public boolean removeUser(int userID) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean logoutUser(int userID) {
        return false; //TODO: Implement this
    }

    @Override
    public List<Pair<User, String>> getAllPasswords() {
        return null; //TODO: Implement this
    }

    @Override
    public boolean storeNewToken(int userID, String token) {
        return false; //TODO: Implement this
    }

    @Override
    public boolean storeNewPassword(int userID, String password) {
        return false; //TODO: Implement this
    }

    @Override
    public List<Integer> getIDs() {
        return null; //TODO: Implement this
    }
}
