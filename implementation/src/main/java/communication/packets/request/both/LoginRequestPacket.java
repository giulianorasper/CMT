package communication.packets.request.both;

import communication.packets.PacketType;
import communication.packets.RequestPacket;
import communication.packets.response.both.LoginResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import user.LoginResponse;
import utils.Pair;

/**
 * This packet handles an login request from either an attendee or an admin and responds with an {@link LoginResponsePacket}.
 */
public class LoginRequestPacket extends RequestPacket {

    public LoginRequestPacket(String username, String password) {
        super(PacketType.LOGIN_REQUEST);
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

    @Override
    public void handle(Conference conference, WebSocket webSocket) {
        Pair<LoginResponse, Pair<String,Long>> result;
        result = conference.login(username, password);

        LoginResponsePacket loginResponsePackage;
        if(result.first() == LoginResponse.Valid) {
            loginResponsePackage = new LoginResponsePacket(result.first(), result.second().first(), result.second().second());
        } else {
            loginResponsePackage = new LoginResponsePacket(result.first());
        }
        loginResponsePackage.send(webSocket);
    }
}
