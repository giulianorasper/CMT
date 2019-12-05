package communication.packets.request;

import communication.enums.RequestResult;
import communication.packets.Packet;
import communication.enums.PacketType;
import communication.packets.RequestPacket;
import communication.packets.ResponsePacket;
import communication.packets.response.LoginResponsePacket;
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

        Packet response;
        if(result.first() == LoginResponse.Valid) {
            response = new LoginResponsePacket(result.second().first(), result.second().second());
        } else {
            response = new ResponsePacket(PacketType.LOGIN_RESPONSE, RequestResult.Failure);
        }
        response.send(webSocket);
    }
}
