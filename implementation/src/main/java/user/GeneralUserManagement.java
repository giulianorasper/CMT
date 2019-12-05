package user;

import utils.Pair;

public interface GeneralUserManagement {

    Pair<LoginResponse, Pair<String, Long>> login(String userName, String password);

    int tokenToID(String token);

    TokenResponse checkToken(String token);

    String getFreeUserName(String name);
}
