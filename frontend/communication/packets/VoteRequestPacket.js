import AuthenticatedRequestPacket from "./AuthenticatedRequestPacket.js";

export default class VoteRequestPacket extends AuthenticatedRequestPacket {

    constructor() {
        super("VOTE_REQUEST");
    }
}
