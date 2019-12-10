package communication;

import com.google.gson.Gson;
import communication.packets.BasePacket;
import communication.enums.PacketType;
import communication.packets.RequestPacket;
import communication.packets.request.*;
import communication.packets.request.admin.*;
import communication.packets.response.FailureResponsePacket;
import main.Conference;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

class WebsocketCommunicationManager extends WebSocketServer implements CommunicationManager {

    private Gson gson = new Gson();
    private Set<WebSocket> conns;
    private Conference conference;
    //TODO implement timeout
    private int timeoutAfter;
    private  boolean debugging;

    public WebsocketCommunicationManager(Conference conference, int port, int timeoutAfter, boolean debugging) {
        super(new InetSocketAddress(port));
        this.conference = conference;
        this.timeoutAfter = timeoutAfter;
        this.debugging = debugging;
        conns = new HashSet<>();
    }

    @Override
    public void onStart() {
        System.out.println("Starting socket server on port: " + getPort());
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
            if(debugging) System.out.println(message);

            RequestPacket pack;
            PacketType packetType = gson.fromJson(message, BasePacket.class).getPacketType();

            switch(packetType) {
                /* ADMIN PACKETS */
                case ADD_ATTENDEE_REQUEST:
                    pack = gson.fromJson(message, AddAttendeeRequestPacket.class);
                    break;
                case ADD_TOPIC_REQUEST:
                    pack = gson.fromJson(message, AddTopicRequestPacket.class);
                    break;
                case ADD_VOTING_REQUEST_PACKET:
                    pack = gson.fromJson(message, AddVotingRequestPacket.class);
                    break;
                case EDIT_USER_REQUEST:
                    pack = gson.fromJson(message, EditUserRequestPacket.class);
                    break;
                case EDIT_VOTING_REQUEST:
                    pack = gson.fromJson(message, EditVotingRequestPacket.class);
                    break;
                case GENERATE_MESSING_ATTENDEE_PASSWORDS:
                    pack = gson.fromJson(message, GenerateMissingAttendeePasswordsRequestPacket.class);
                    break;
                case GENERATE_NEW_ATTENDEE_PASSWORD:
                    pack = gson.fromJson(message, GenerateNewAttendeePasswordRequestPacket.class);
                    break;
                case GET_ALL_ATTENDEE_PASSWORDS:
                    pack = gson.fromJson(message, GetAllAttendeePasswordsRequestPacket.class);
                    break;
                case GET_ALL_ATTENDEES_REQUEST:
                    pack = gson.fromJson(message, GetAllAttendeesRequestPacket.class);
                    break;
                case GET_ALL_REQUESTS_REQUEST:
                    pack = gson.fromJson(message, GetAllRequestsRequestPacket.class);
                    break;
                case GET_ATTENDEE_DATA_REQUEST:
                    pack = gson.fromJson(message, GetAttendeeDataRequestPacket.class);
                    break;
                case GET_ATTENDEE_PASSWORD_REQUEST:
                    pack = gson.fromJson(message, GetAttendeePasswordRequestPacket.class);
                    break;
                case GET_VOTINGS_REQUEST:
                    pack = gson.fromJson(message, GetVotingsRequestPacket.class);
                    break;
                case LOGOUT_ALL_ATTENDEES:
                    pack = gson.fromJson(message, LogoutAllAttendeesRequestPacket.class);
                    break;
                case LOGOUT_ATTENDEE_REQUEST:
                    pack = gson.fromJson(message, LogoutAttendeeRequestPacket.class);
                    break;
                case REMOVE_ATTENDEE_REQUEST:
                    pack = gson.fromJson(message, RemoveAttendeeRequestPacket.class);
                    break;
                case REMOVE_TOPIC_REQUEST:
                    pack = gson.fromJson(message, RemoveTopicRequestPacket.class);
                    break;
                case REMOVE_VOTING_REQUEST:
                    pack = gson.fromJson(message, RemoveVotingRequestPacket.class);
                    break;
                case RENAME_TOPIC_REQUEST:
                    pack = gson.fromJson(message, RenameTopicRequestPacket.class);
                    break;
                case REORDER_TOPIC_REQUEST:
                    pack = gson.fromJson(message, ReorderTopicRequestPacket.class);
                    break;
                case SET_ATTENDEE_PRESENT_STATUS_REQUEST:
                    pack = gson.fromJson(message, SetAttendeePresentStatusRequestPacket.class);
                    break;
                case SET_REQUEST_APPROVAL_STATUS:
                    pack = gson.fromJson(message, SetRequestApprovalStatusRequestPacket.class);
                    break;
                case START_VOTING_REQUEST:
                    pack = gson.fromJson(message, StartVotingRequestPacket.class);
                    break;
                case UPDATE_FILE_REQUEST:
                    pack = gson.fromJson(message, UpdateFileRequestPacket.class);
                    break;
                    /* USER PACKETS */
                case ADD_VOTE_REQUEST:
                    pack = gson.fromJson(message, AddVoteRequestPacket.class);
                    break;
                case DOWNLOAD_FILE_REQUEST:
                    pack = gson.fromJson(message, DownloadFileRequestPacket.class);
                    break;
                case GET_ACTIVE_VOTING_REQUEST:
                    pack = gson.fromJson(message, GetActiveVotingRequestPacket.class);
                    break;
                case GET_AGENDA_REQUEST:
                    pack = gson.fromJson(message, GetAgendaRequestPacket.class);
                    break;
                case CONFERENCE_DATA_REQUEST:
                    pack = gson.fromJson(message, GetConferenceDataRequestPacket.class);
                    break;
                case IS_ADMIN_REQUEST:
                    pack = gson.fromJson(message, IsAdminRequestPacket.class);
                    break;
                case LOGIN_REQUEST:
                    pack = gson.fromJson(message, LoginRequestPacket.class);
                    break;
                case PERSONAL_DATA_REQUEST:
                    pack = gson.fromJson(message, PersonalDataRequestPacket.class);
                    break;
                case REQUEST_OF_CHANGE_REQUEST:
                    pack = gson.fromJson(message, RequestOfChangeRequestPacket.class);
                    break;
                case REQUEST_OF_SPEECH_REQUEST:
                    pack = gson.fromJson(message, RequestOfSpeechRequestPacket.class);
                    break;
                default:
                    throw new IllegalArgumentException("Packet type " + packetType + " does not exist.");
            }
            pack.handle(conference, conn);


        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                e.printStackTrace();
                new FailureResponsePacket(e.getMessage()).send(conn);
            } else {
                e.printStackTrace();
                //TODO this should basically never happen, therefore log occurrences
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        UpdateFileRequestPacket packet = UpdateFileRequestPacket.getRequestFromConnectionIfExists(conn);
        if(packet != null) {
            byte[] fileBytes = message.array();
            packet.handleFileTransfer(conference, conn, fileBytes);
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
