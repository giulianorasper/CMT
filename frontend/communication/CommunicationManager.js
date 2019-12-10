var timeout = 2000;

export default class CommunicationManager {

    static openConnection() {
        //TODO make this configurable
        var socket;
        socket = new WebSocket('ws://127.0.0.1:17699');
        return socket;
    }

    static send(packet, successHook = new function(packet){}, errorHook = new function(){}) {
        var socket = CommunicationManager.openConnection();
        function onmessage(event) {
            console.log(event.data);
            let responsePacket = JSON.parse(event.data);
            successHook(responsePacket);
            socket.onclose = function() {};
            socket.close();
        };
        if(packet.packetType === "UPDATE_FILE_REQUEST") {
            let fileBytes = packet.fileBytes;
            packet.fileBytes = null;
            socket.onmessage = function (event) {
                console.log(event.data);
                var responsePacket = JSON.parse(event.data);
                if(responsePacket.result === "Valid") {
                    socket.onmessage = onmessage;
                    socket.send(fileBytes);
                } else {
                    onmessage(event);
                }
            }
        } else {
            socket.onmessage = onmessage;
        }
        socket.onopen = () => {
            socket.send(JSON.stringify(packet));
        };
        socket.onclose = errorHook;
        socket.onerror = errorHook;
    }
}