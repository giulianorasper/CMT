import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class UpdateFileRequestPacket extends AuthenticatedRequestPacket {

    constructor(name) {
        super("DELETE_FILE_REQUEST");
        this.name = name;
    }
}
