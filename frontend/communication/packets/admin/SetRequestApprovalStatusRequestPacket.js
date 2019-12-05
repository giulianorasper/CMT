import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class RemoveVotingRequestPacket extends AuthenticatedRequestPacket {

    constructor(id, approved) {
        super("SET_REQUEST_APPROVAL_STATUS");
        this.id = id;
        this.approved = approved;
    }
}
