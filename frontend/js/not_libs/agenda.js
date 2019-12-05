import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAgendaRequestPacket from "../../communication/packets/GetAgendaRequestPacket.js";



$( document ).ready(function() {

    var agendaContainer = $('#agendaContainer');

    const packet = new GetAgendaRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet) {
        if(packet.result === "Valid") {
            renderAgenda(packet.agenda, agendaContainer);
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }


});

function renderAgenda(data, $e) {
    // create an inner item
    var fontSize = 26;
    var fontSizeDifference = 4;
    function createInner(obj, $target) {
        var li = generateAgendaRow().appendTo($target);
        li.text(obj.name);
        if (obj.subTopics.topics != undefined && obj.subTopics.topics.length > 0) {
            var innerList = $('<div class="list"><ul>').appendTo(li);
            for (var i = 0; i < obj.subTopics.topics.length; i++) {
                var child = obj.subTopics.topics[i];
                fontSize = fontSize - fontSizeDifference
                createInner(child, innerList);
            }
            fontSize = fontSize + fontSizeDifference
        }
    }
    for (var i = 0; i < data.topics.length; i++) {
        createInner(data.topics[i], $e);
    }
}

function generateAgendaRow(fontSize){
    return $('<li style="font-size: '+fontSize+'px;">' + '<span class="glyphicon glyphicon-print"></span>');
}




