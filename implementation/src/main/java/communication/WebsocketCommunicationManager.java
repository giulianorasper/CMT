package communication;

import com.google.gson.Gson;
import communication.packets.BasePacket;
import communication.packets.Packet;
import communication.packets.PacketType;
import communication.packets.RequestPacket;
import communication.packets.request.admin.AddTopicRequestPacket;
import communication.packets.request.admin.RemoveTopicRequestPacket;
import communication.packets.request.admin.RenameTopicRequestPacket;
import communication.packets.request.admin.ReorderTopicRequestPacket;
import communication.packets.request.attendee.GetActiveVotingRequestPacket;
import communication.packets.request.attendee.PersonalDataRequestPacket;
import communication.packets.request.attendee.VoteRequestPacket;
import communication.packets.request.both.GetAgendaRequestPacket;
import communication.packets.request.both.LoginRequestPacket;
import main.Conference;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class WebsocketCommunicationManager extends WebSocketServer implements CommunicationManager {

    private Gson gson = new Gson();
    //TODO make this configurable
    private static int TCP_PORT = 17699;
    private Set<WebSocket> conns;
    private Conference conference;
    //TODO implement timeout
    private int timeoutAfter;

    public WebsocketCommunicationManager(Conference conference, int timeoutAfter) {
        super(new InetSocketAddress(TCP_PORT));
        init(conference, timeoutAfter);
        conns = new HashSet<>();
    }

    @Override
    public void init(Conference conference, int timeoutAfter) {
        this.conference = conference;
        this.timeoutAfter = timeoutAfter;
    }

    @Override
    public void onStart() {
        System.out.println("Starting socket server on port: " + TCP_PORT);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println(System.currentTimeMillis() + ": New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    //TODO connection closed during request?
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            RequestPacket pack;
            PacketType packetType = gson.fromJson(message, BasePacket.class).getPacketType();

            switch(packetType) {
                case LOGIN_REQUEST:
                    pack = gson.fromJson(message, LoginRequestPacket.class);
                    break;
                case GET_ACTIVE_VOTING_REQUEST:
                    pack = gson.fromJson(message, GetActiveVotingRequestPacket.class);
                    break;
                case PERSONAL_DATA_REQUEST:
                    pack = gson.fromJson(message, PersonalDataRequestPacket.class);
                    break;
                case VOTE_REQUEST:
                    pack = gson.fromJson(message, VoteRequestPacket.class);
                    break;
                case GET_AGENDA_REQUEST:
                    pack = gson.fromJson(message, GetAgendaRequestPacket.class);
                    break;
                case ADD_TOPIC_REQUEST:
                    pack = gson.fromJson(message, AddTopicRequestPacket.class);
                    break;
                case REMOVE_TOPIC_REQUEST:
                    pack = gson.fromJson(message, RemoveTopicRequestPacket.class);
                    break;
                case RENAME_TOPIC_REQUEST:
                    pack = gson.fromJson(message, RenameTopicRequestPacket.class);
                    break;
                case REORDER_TOPIC_REQUEST:
                    pack = gson.fromJson(message, ReorderTopicRequestPacket.class);
                    break;
                default:
                    throw new IllegalArgumentException("Packet type " + packetType + " does not exist.");
            }
            pack.handle(conference, conn);


        } catch (Exception e) {
            e.printStackTrace();
            //TODO this should basically never happen, therefore log occurrences
        }
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
            // do some thing if required
        }
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        //TODO handle open requests and only then call stop causing the socket server to stop and close all connections
        super.stop();
    }
}
