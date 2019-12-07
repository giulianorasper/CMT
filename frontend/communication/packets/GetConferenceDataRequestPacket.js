import AuthenticatedRequestPacket from "./AuthenticatedRequestPacket.js";

export default class PersonalDataRequestPacket extends AuthenticatedRequestPacket {

    constructor() {
        super("CONFERENCE_DATA_REQUEST");
    }
}
