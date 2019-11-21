package communication.packages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.java_websocket.WebSocket;

public class BasePacket implements Packet {

    private PacketType packetType;

    public BasePacket(PacketType packetType) {
        this.packetType = packetType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public String toJson() {
        //TODO move Gson object to a static context
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public void send(WebSocket socket) {
        socket.send(toJson());
    }

}
