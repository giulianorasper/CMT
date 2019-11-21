package communication.packages.response;

import communication.packages.PacketType;
import communication.packages.ResponsePacket;
import login.LoginResponse;

public class LoginResponsePacket extends ResponsePacket {

    private LoginResponse loginResponse;
    private String token;
    private long expiration;

    public LoginResponsePacket(LoginResponse loginResponse, String token, long expiration) {
        super(PacketType.LOGIN_RESPONSE);
        this.loginResponse = loginResponse;
        this.token = token;
        this.expiration = expiration;
    }

    public LoginResponsePacket(LoginResponse loginResponse) {
        this(loginResponse, null, -1);
    }
}
