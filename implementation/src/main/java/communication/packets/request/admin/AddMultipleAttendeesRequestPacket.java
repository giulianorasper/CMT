package communication.packets.request.admin;

import communication.enums.PacketType;
import communication.packets.AuthenticatedRequestPacket;
import communication.packets.response.ValidResponsePacket;
import communication.wrapper.Connection;
import main.Conference;
import org.java_websocket.WebSocket;
import user.Attendee;

import java.util.List;

public class AddMultipleAttendeesRequestPacket extends AuthenticatedRequestPacket {

    private List<SimpleAttendee> attendees;

    public AddMultipleAttendeesRequestPacket(List<SimpleAttendee> attendees) {
        super(PacketType.ADD_MULTIPLE_ATTENDEES_REQUEST);
        this.attendees = attendees;
    }

    @Override
    public void handle(Conference conference, Connection webSocket) {
        if(isPermitted(conference, webSocket, true)) {
            attendees.forEach(a -> {
                if(a.getName() == null) throw new IllegalArgumentException();
            });
            attendees.forEach(a -> {
                Attendee attendee = new Attendee(a.getName(), a.getEmail(), conference.getFreeUserName(a.getName()), a.getGroup(), a.getResidence(), a.getFunction());
                conference.addAttendee(attendee);
            });
            new ValidResponsePacket().send(webSocket);
        }
    }

    class SimpleAttendee {
        private String name;
        private String email;
        private String group;
        private String residence;
        private String function;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getGroup() {
            return group;
        }

        public String getResidence() {
            return residence;
        }

        public String getFunction() {
            return function;
        }
    }
}
