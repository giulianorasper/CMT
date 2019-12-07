import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAgendaRequestPacket from "../../communication/packets/GetAgendaRequestPacket.js";
import RequestOfSpeechRequestPacket from "../../communication/packets/RequestOfSpeechRequestPacket.js";

var speachRequestOptions = $("#speachRequestOption");

$( document ).ready(function() {
	getAgenda();

	window.submitRequest = submit;
});

function getAgenda(){
 	const packet = new GetAgendaRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet){
    	if(packet.result === "Valid") {          
        	var agenda = packet.agenda;
        	for (var i = 0; i < agenda.topics.length; i++) {
        		addTopic(agenda.topics[i], (i+1));
    		}

    	}

    	function addTopic(topic, preorder){
    		$("<option data-id=\""+preorder+"\" data-isTop =  true'>" +preorder+" "+topic.name+"</option>").appendTo(speachRequestOptions);
    		for (var i = 0; i < topic.subTopics.topics.length; i++) {
                var child = topic.subTopics.topics[i];
                addTopic(child, preorder+"."+(i+1));
            }
    	}

    }

    function fail(){
    	console.log("This method is called if something went wrong during the general communication.");
    }
}

function submit(){
	var selectedOption = speachRequestOptions.find('option:selected');

	var refersToTopic = selectedOption.attr("data-isTop");
	var reference = selectedOption.attr("data-id");

	const packet = new RequestOfSpeechRequestPacket(refersToTopic, reference);

    CommunicationManager.send(packet, success, fail);

     function success(packet){
    	if(packet.result === "Valid") {          
        	alert("Your request has been succesfully submited");
    	}
    }


    function fail(){
    	console.log("This method is called if something went wrong during the general communication.");
    }
}

function getDocuments(){
	//TODO
}
