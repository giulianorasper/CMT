import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class GetVotingsRequestPacket extends AuthenticatedRequestPacket {

    constructor() {
        super("GET_PREVIOUS_VOTINGS_REQUEST");
    }
}
