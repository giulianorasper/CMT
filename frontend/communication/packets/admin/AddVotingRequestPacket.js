import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class AddVotingRequestPacket extends AuthenticatedRequestPacket {

    constructor(question, options, namedVote) {
        super("ADD_VOTING_REQUEST_PACKET");
        this.question = question;
        this.options = options;
        this.namedVote = namedVote;
    }
}
