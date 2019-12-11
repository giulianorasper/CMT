import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAgendaRequestPacket from "../../communication/packets/GetAgendaRequestPacket.js";
import RequestOfSpeechRequestPacket from "../../communication/packets/RequestOfSpeechRequestPacket.js";
import RequestOfChangeRequestPacket from "../../communication/packets/RequestOfChangeRequestPacket.js";

var changeMessage = $("#requestMessage");
var requestOptions = $(".requestSelect");


$( document ).ready(function() {
	getAgenda();

	window.submitRequest = submit;
});


function submit(isSpeechRequest){
	var selectedOption = requestOptions.find('option:selected');


    if( !selectedOption.attr("data-isTop")){
        alert("Please select a topic");
        return;
    }

	var refersToTopic = selectedOption.attr("data-isTop");
	var reference = selectedOption.attr("data-id");

	var packet;
	if(isSpeechRequest){
		packet = new RequestOfSpeechRequestPacket(refersToTopic, reference);
	}
	else{
        if(!changeMessage.val()){
            alert("Please enter a message or submit a speech request");
            return;
        }
		packet = new RequestOfChangeRequestPacket(refersToTopic, reference, changeMessage.val());
	}

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
    		requestOptions.each(function(i, option){
	    		$("<option data-id=\""+preorder+"\" data-isTop = true>" +preorder+" "+topic.name+"</option>").appendTo(option);
        		for (var i = 0; i < topic.subTopics.topics.length; i++) {
                    var child = topic.subTopics.topics[i];
                    addTopic(child, preorder+"."+(i+1));
                }
    	   });

        }
    }

    function fail(){
    	console.log("This method is called if something went wrong during the general communication.");
    }
}

function getDocuments(){
	//TODO
}
