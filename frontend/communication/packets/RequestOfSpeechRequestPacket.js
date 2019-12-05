import AuthenticatedRequestPacket from "./AuthenticatedRequestPacket.js";

export default class RequestOfSpeechRequestPacket extends AuthenticatedRequestPacket {

    constructor(refersToTopic, reference) {
        super("REQUEST_OF_CHANGE_REQUEST");
        this.refersToTopic = refersToTopic;
        this.reference = reference;
    }
}
