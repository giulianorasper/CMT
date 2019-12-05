import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAgendaRequestPacket from "../../communication/packets/GetAgendaRequestPacket.js";
import AddTopicRequestPacket from "../../communication/packets/admin/AddTopicRequestPacket.js";
import RemoveTopicRequestPacket from "../../communication/packets/admin/RemoveTopicRequestPacket.js";
import RenameTopicRequestPacket from "../../communication/packets/admin/RenameTopicRequestPacket.js";

var agendaContainer = $('#agendaContainer');

$( document ).ready(function() {

    window.appendToAgenda = append// export the function to the global scope
    window.subtopicToAgenda = subtopic// export the function to the global scope
    window.removeFromAgenda = remove// export the function to the global scope
    window.editAgenda = edit// export the function to the global scope

    const packet = new GetAgendaRequestPacket();

    CommunicationManager.send(packet, successAgendaReq, failAgendaReq);



});


 function successAgendaReq(packet) {
    if(packet.result === "Valid") {          
        renderAgenda(packet.agenda, agendaContainer);
    }
}

function failAgendaReq() {
    console.log("This method is called if something went wrong during the general communication.");
}

function renderAgenda(data, $e) {
    // create an inner item
    agendaContainer.html("");
    var fontSize = 26;
    var fontSizeDifference = 4;
    function createInner(obj, $target, preOrder) {
        var li = generateAgendaRow(obj.name, preOrder, fontSize).appendTo($target);
        fontSize = fontSize - fontSizeDifference
        if (obj.subTopics.topics != undefined && obj.subTopics.topics.length > 0) {
            var innerList = $('<div class="list"><ul>').appendTo(li);
            for (var i = 0; i < obj.subTopics.topics.length; i++) {
                var child = obj.subTopics.topics[i];
                createInner(child, innerList,  preOrder+"."+(i+1));
            }
        }
        fontSize = fontSize + fontSizeDifference
    }
    for (var i = 0; i < data.topics.length; i++) {
        createInner(data.topics[i], $e, i+1);
    }
}


function append(preorder){
    var split = (""+preorder).split(".");
    var elem = split.pop()
    var newOrder = split + (parseInt(elem) +1);
    alert(newOrder);


    const packet = new AddTopicRequestPacket(newOrder, prompt("topic name?"));

    function success(packet) {
        console.log(packet);
        if(packet.result === "Valid") {
                const packet = new GetAgendaRequestPacket();
                CommunicationManager.send(packet, successAgendaReq, failAgendaReq);
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }


    CommunicationManager.send(packet, success, fail);
}

function remove(preorder){

    const packet = new RemoveTopicRequestPacket(preorder);

    function success(packet) {
        if(packet.result === "Valid") {
                const packet = new GetAgendaRequestPacket();
                CommunicationManager.send(packet, successAgendaReq, failAgendaReq);
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }

    
    CommunicationManager.send(packet, success, fail);
}

function subtopic(preorder){
    append(preorder+".0");
}
 
function edit(preorder){
    const packet = new RenameTopicRequestPacket(preorder, prompt("topic name?"));

    function success(packet) {
        console.log(packet);
        if(packet.result === "Valid") {
                const packet = new GetAgendaRequestPacket();
                CommunicationManager.send(packet, successAgendaReq, failAgendaReq);
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }


    CommunicationManager.send(packet, success, fail);
} 


function generateAgendaRow(name, preorder, fontSize){
    return $(
        '<li style="font-size: '+fontSize+'px;">'+name+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-plus" onclick = "appendToAgenda('+preorder+')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-chevron-down" onclick = "subtopicToAgenda('+preorder+')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-pencil" onclick = "editAgenda('+preorder+')"></span>'+
         '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-trash" onclick = "removeFromAgenda('+preorder+')"></span>'+
        '</li>');
}




