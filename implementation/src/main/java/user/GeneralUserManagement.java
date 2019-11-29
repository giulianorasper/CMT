package user;

import utils.Pair;

public interface GeneralUserManagement {

    Pair<LoginResponse, Pair<String, Long>> login(String userName, String password);
}
