package communication.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.java_websocket.WebSocket;

/**
 * An implementation of {@link Packet} which introduces an constructor, forcing subclasses to specify their {@link PacketType}.
 */
public class BasePacket implements Packet {

    private PacketType packetType;

    /**
     * Initializes the packet by setting it's {@link PacketType}.
     * @param packetType the {@link PacketType} of this packet
     */
    public BasePacket(PacketType packetType) {
        this.packetType = packetType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    /**
     * Converts this packet to a JSON equivalent.
     * @return JSON String of this packet
     */
    public String toJson() {
        //TODO move Gson object to a static context
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    public void send(WebSocket socket) {
        socket.send(toJson());
    }

}
