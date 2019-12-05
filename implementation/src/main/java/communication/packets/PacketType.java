package communication.packets;

//TODO refine packet types to a final standard
/**
 * This enum contains all types of packets as String representation.
 * This String is used on the server-side to identify a {@link Packet} before instantiating the corresponding class.
 * However, for increased code flexibility the types includes those of {@link ResponsePacket}'s despite this information is currently not needed on the client-side.
 */
public enum PacketType {
    LOGIN_REQUEST, LOGIN_RESPONSE, PERSONAL_DATA_REQUEST, PERSONAL_DATA_RESPONSE, GET_AGENDA_REQUEST, GET_AGENDA_RESPONSE, VOTE_REQUEST, VOTE_RESPONSE,
    GET_ACTIVE_VOTING_REQUEST, GET_ACTIVE_VOTING_RESPONSE, INVALID_TOKEN, FAILURE, NO_PERMISSION, ADD_TOPIC_REQUEST, REMOVE_TOPIC_REQUEST,
    RENAME_TOPIC_REQUEST, REORDER_TOPIC_REQUEST, VALID_RESPONSE, REQUEST_OF_CHANGE_REQUEST, REQUEST_OF_SPEECH_REQUEST, GET_ALL_REQUESTS_REQUEST,
    GET_ALL_REQUESTS_RESPONSE, ADD_ATTENDEE_REQUEST;
}
