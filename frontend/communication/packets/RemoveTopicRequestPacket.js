import AuthenticatedRequestPacket from "./AuthenticatedRequestPacket.js";

export default class RemoveTopicRequestPacket extends AuthenticatedRequestPacket {

    
    constructor(position) {
        super("REORDER_TOPIC_REQUEST");
        this.position = position;
    }
}
