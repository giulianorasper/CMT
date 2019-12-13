import {ip, port, useSSL} from "../config/config.js"
var timeout = 2000;

export default class CommunicationManager {

    static openConnection() {
        var socket;
        var address;
        if(useSSL) {
            address = "wss://"
        } else {
            address = "ws://"
        }
        const endpoint = "websocket";
        address = address + ip + ":" + port + "/" + endpoint;
        console.log(address);
        socket = new WebSocket(address);
        return socket;
    }

    static send(packet, successHook = new function(packet){}, errorHook = new function(){}) {
        var socket = CommunicationManager.openConnection();
        function onmessage(event) {
         //   console.log(event.data);
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