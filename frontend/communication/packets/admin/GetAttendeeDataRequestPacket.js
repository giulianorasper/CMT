import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class LogoutAttendeeRequestPacket extends AuthenticatedRequestPacket {

    constructor(userID) {
        super("GET_ATTENDEE_DATA_REQUEST");
        this.id = userID;
    }
}
