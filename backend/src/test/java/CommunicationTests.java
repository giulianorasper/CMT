import com.google.gson.Gson;
import communication.packets.request.LoginRequestPacket;
import org.junit.Test;

public class CommunicationTests {

    @Test
    public void justASample(){
        //LoginRequestPacket l = new LoginRequestPacket("theAnswer", "42");
        String s = "{\n" +
                "  \"username\": \"theAnswer\",\n" +
                "  \"password\": \"42\",\n" +
                "  \"packetType\": \"LOGIN_REQUEST\"\n" +
                "}";
        LoginRequestPacket l = new Gson().fromJson(s, LoginRequestPacket.class);
        System.out.println(l.toJson());
    }
}
