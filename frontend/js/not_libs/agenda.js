import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAgendaRequestPacket from "../../communication/packets/GetAgendaRequestPacket.js";
import AddTopicRequestPacket from "../../communication/packets/admin/AddTopicRequestPacket.js";
import RemoveTopicRequestPacket from "../../communication/packets/admin/RemoveTopicRequestPacket.js";
import RenameTopicRequestPacket from "../../communication/packets/admin/RenameTopicRequestPacket.js";
import IsAdminRequestPacket from "../../communication/packets/IsAdminRequestPacket.js";

var agendaContainer = $('#agendaContainer');

$( document ).ready(function() {

    window.appendToAgenda = newTopic// export the function to the global scope
    window.subtopicToAgenda = newSubtopic// export the function to the global scope
    window.removeFromAgenda = remove// export the function to the global scope
    window.editAgenda = edit// export the function to the global scope

    checkAdminStatus();

    const packet = new GetAgendaRequestPacket();

    CommunicationManager.send(packet, successAgendaReq, failAgendaReq);

});


 function successAgendaReq(packet) {
    if(packet.result === "Valid") {      
        checkAdminStatus();
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

    var fontSize = 32;
    var fontSizeDifference = 2;
    var fontSizeMin = 26;
    
    function createInner(obj, $target, preOrder) {
        var li = generateAgendaRow(obj.name, preOrder, fontSize).appendTo($target);

        var decreased;

        if(fontSize >= fontSizeMin){
            fontSize -= fontSizeDifference;
            decreased = true;
        } else{
            decreased = false;
        }

        if (obj.subTopics.topics !== undefined && obj.subTopics.topics.length > 0) {
            var innerList = $('<ul class="list agendaList"></ul>').appendTo(li);
            for (var i = 0; i < obj.subTopics.topics.length; i++) {
                var child = obj.subTopics.topics[i];
                createInner(child, innerList,  preOrder+"."+(i+1));
            }

            if(decreased){
                fontSize += fontSizeDifference;
            }
        }
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


function append(preorder, isSubtopic){
    var split = (""+preorder).split(".");
    var elem = split.pop()
    var newOrder = (parseInt(elem) +1);
    if(split.length !== 0){
        newOrder = split.join(".")+ "." + newOrder
    }

    var res;
    if(isSubtopic){
        res = prompt("Enter name of new subtopic")
    } else{
        res = prompt("Enter name of new topic");
    }

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

function newSubtopic(preorder){
    append(preorder+".0", true);
}

function newTopic(preorder){
     append(preorder, false);
}
 
function edit(preorder){
    var res = prompt("Enter new topic name");
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
        '</span><span style="font-size:32px;" class="glyphicon glyphicon-plus" onclick = "appendToAgenda(\''+preorder+'\')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span style="font-size:32px" class="glyphicon glyphicon-chevron-down" onclick = "subtopicToAgenda(\''+preorder+'\')"></span>'+
        '<span style="display:inline-block; width: 30px;">'+
        '</span><span style="font-size:32px" class="glyphicon glyphicon-pencil" onclick = "editAgenda(\''+preorder+'\')"></span>'+
         '<span style="display:inline-block; width: 30px;">'+
        '</span><span style="font-size:32px" class="glyphicon glyphicon-trash" onclick = "removeFromAgenda(\''+preorder+'\')"></span>':"")+
        '</li>');
}



function checkAdminStatus(){
    const packet = new IsAdminRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet) {
        console.log(packet);
        if(packet.result === "Valid") {

            if(!packet.admin){
                $(".adminField").each(function(i, field){
                    console.log(field)
                    $(field).css("display", "none");
                })
            }
            window.isAdmin = packet.admin;          
            
        }
        else if(packet.result =="InvalidToken"){
             window.location = "./index.html"
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }
}

