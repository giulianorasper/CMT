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
    if(data.topics.length === 0){
        createDefault($e);
        return;
    }

    var fontSize = 26;
    var fontSizeDifference = 4;
    
    function createInner(obj, $target, preOrder) {
        var li = generateAgendaRow(obj.name, preOrder, fontSize).appendTo($target);
        fontSize = fontSize - fontSizeDifference
        if (obj.subTopics.topics !== undefined && obj.subTopics.topics.length > 0) {
            var innerList = $('<ul class="list agendaList"></ul>').appendTo(li);
            for (var i = 0; i < obj.subTopics.topics.length; i++) {
                var child = obj.subTopics.topics[i];
                createInner(child, innerList,  preOrder+"."+(i+1));
            }
        }
        fontSize = fontSize + fontSizeDifference
    }

    function createDefault(target){
       if(window.isAdmin) {$("<div class=\"row \">"+

                            "<div class=\"form-group mt-3 col-lg-12\" style=\"float: left;\">"+
                            "    <button class=\"button button-contactForm boxed-btn \" onclick=\"appendToAgenda(\'0\')\">Add Topic</button>"+
                            "</div>"+
                        "</div>").appendTo(target);}
        else{
            $("<div class=\"row \">"+

                            "<div class=\"form-group mt-3 col-lg-12\" style=\"float: left;\">"+
                            "    Currently the agenda is empty"+
                            "</div>"+
                        "</div>").appendTo(target);
        }
    

    }

    for (var i = 0; i < data.topics.length; i++) {
        createInner(data.topics[i], $e, i+1);
    }
}


function append(preorder){
    var split = (""+preorder).split(".");
    var elem = split.pop()
    var newOrder = (parseInt(elem) +1);
    if(split.length !== 0){
        newOrder = split.join(".")+ "." + newOrder
    }

    var res = prompt("topic name?");

    if(res){
        const packet = new AddTopicRequestPacket(newOrder, res);
        CommunicationManager.send(packet, success, fail);
    }

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
    var res = prompt("topic name?");
    if(res){
        const packet = new RenameTopicRequestPacket(preorder, res);
        CommunicationManager.send(packet, success, fail);
    }

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


} 


function generateAgendaRow(name, preorder, fontSize){
    return $(
        '<li class = "agendaRow" style="font-size: '+fontSize+'px;">'+name+(window.isAdmin?
        '<span style="display:inline-block; width: 60px;">'+
        '</span><span class="glyphicon glyphicon-plus" onclick = "appendToAgenda(\''+preorder+'\')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-chevron-down" onclick = "subtopicToAgenda(\''+preorder+'\')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-pencil" onclick = "editAgenda(\''+preorder+'\')"></span>'+
         '<span style="display:inline-block; width: 30px;">'+
        '</span><span class="glyphicon glyphicon-trash" onclick = "removeFromAgenda(\''+preorder+'\')"></span>':"")+
        '</li>');
}




