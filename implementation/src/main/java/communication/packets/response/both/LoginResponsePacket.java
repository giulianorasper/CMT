package communication.packets.response.both;

import communication.packets.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.request.both.LoginRequestPacket;
import user.LoginResponse;

/**
 * A login response sent as result of an login request using an {@link LoginRequestPacket}.
 */
public class LoginResponsePacket extends ResponsePacket {

    private LoginResponse loginResponse;
    private String token;
    private long expiration;

    /**
     *
     * @param loginResponse A String representing the overall result of the login request. This {@link LoginResponse} may be an abstraction of the actual response.
     * @param token A token used for further communication on a successful login, null otherwise.
     * @param expiration The number of seconds until the token should expire.
     */
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
