import AuthenticatedRequestPacket from "./AuthenticatedRequestPacket.js";

export default class PersonalDataRequestPacket extends AuthenticatedRequestPacket {

    constructor() {
        super("IS_ADMIN_REQUEST");
    }
}
