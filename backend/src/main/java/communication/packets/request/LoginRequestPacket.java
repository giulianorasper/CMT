package communication.packets.request;

import communication.CommunicationFactory;
import communication.packets.PacketType;
import communication.packets.RequestPacket;
import communication.packets.response.LoginResponsePacket;
import login.LoginResponse;
import org.java_websocket.WebSocket;
import utils.Pair;

public class LoginRequestPacket extends RequestPacket {

    public LoginRequestPacket(String username, String password) {
        super(PacketType.LOGIN_REQUEST);
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

    @Override
    public void handle(CommunicationFactory factory, WebSocket webSocket) {
        Pair<LoginResponse, Pair<String,Long>> result;
        //result = factory.checkLogin(username, password);
        //Just for the prototype
        if(username.equals("theAnswer") && password.equals("42")) {
            result = new Pair<>(LoginResponse.Valid, new Pair<>("suchSecureWow", System.currentTimeMillis() + 2000));
        } else {
            result = new Pair<>(LoginResponse.WrongPassword, null);
        }

        //actual code again
        LoginResponsePacket loginResponsePackage;
        if(result.getFirst() == LoginResponse.Valid) {
            loginResponsePackage = new LoginResponsePacket(result.getFirst(), result.getSecond().getFirst(), result.getSecond().getSecond());
        } else {
            loginResponsePackage = new LoginResponsePacket(result.getFirst());
        }
        loginResponsePackage.send(webSocket);
    }
}
