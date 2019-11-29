package user;

public enum LoginResponse {
    Valid,
    WrongPassword,
    UserDoesNotExist,
    AccountBlocked,
    PasswordAlreadyUsed;
}
