package user;

import utils.Pair;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_GeneralUserManagement {

    Pair<LoginResponse, String> checkLogin(String name, String password);

    TokenResponse checkToken (String token);

    boolean removeUser(int userID);

    boolean logoutUser(int userID);

    List<Pair<User, String>> getAllPasswords();

    boolean storeNewToken(int userID, String token);

    boolean storeNewPassword(int userID, String password);

    List<Integer> getIDs();
}
