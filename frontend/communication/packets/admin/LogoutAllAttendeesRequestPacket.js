import LogoutAllAttendeesRequestPacket from "../AuthenticatedRequestPacket.js";

export default class GetAllAttendeesRequestPacket extends AuthenticatedRequestPacket {

    constructor() {
        super("LOGOUT_ALL_ATTENDEES");
    }
}
