var timeout = 2000;

export default class CommunicationManager {

    static openConnection() {
        //TODO make this configurable
        var socket;
        socket = new WebSocket('ws://127.0.0.1:17699');
        return socket;
    }

    static send(packet, successHook = new function(packet){}, errorHook = new function(){}) {
        var socket = new WebSocket('ws://127.0.0.1:17699');
        socket.onmessage = function(event) {
            successHook(JSON.parse(event.data));
            socket.onclose = function() {};
            socket.close();
        };
        socket.onopen = () => {
            socket.send(JSON.stringify(packet));
        };
        socket.onclose = errorHook;
        socket.onerror = errorHook;
    }
}