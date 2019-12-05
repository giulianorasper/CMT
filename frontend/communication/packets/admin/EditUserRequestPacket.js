import AuthenticatedRequestPacket from "../AuthenticatedRequestPacket.js";

export default class EditUserRequestPacket extends AuthenticatedRequestPacket {

    constructor(name, email, group, residence, Function) {
        super("EDIT_USER_REQUEST");
        this.email = email;
        this.name = name;
        this.group = group;
        this.residence = residence;
        this.function = Function;
    }
}
