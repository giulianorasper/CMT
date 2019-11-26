package communication.packets.response.attendee;
import communication.packets.PacketType;
import communication.packets.ResponsePacket;
import communication.packets.request.attendee.PersonalDataRequest;

/**
 * A response containing personal data of an attendee which is sent as result of their {@link PersonalDataRequest}.
 */
public class PersonalDataResponse extends ResponsePacket {

    private String firstname;
    private String lastname;
    private String group;
    private String function;
    private String email;
    private String residence;

    /**
     *
     * @param firstname the attendee's firstname
     * @param lastname the attendee's lastname
     * @param group the attendee's group
     * @param function the attendee's function in their group
     * @param email the attendee's mail address
     * @param residence the attendee's residence address
     */
    public PersonalDataResponse(String firstname, String lastname, String group, String function, String email, String residence) {
        super(PacketType.PERSONAL_DATA_RESPONSE);
        this.firstname = firstname;
        this.lastname = lastname;
        this.group = group;
        this.function = function;
        this.email = email;
        this.residence = residence;
    }
}
