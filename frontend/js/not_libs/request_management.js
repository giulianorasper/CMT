import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAllRequestsRequestPacket from "../../communication/packets/admin/GetAllRequestsRequestPacket.js";

var requestContainer = $('#requestContainer');

$( document ).ready(function() {

	const packet = new GetAllRequestsRequestPacket();
    CommunicationManager.send(packet, success, fail);


 	function success(packet) {
	    if(packet.result === "Valid") {          
	        for(var req of packet.requests){
	        	 generateRequest(req).appendTo(requestContainer);
	        }
	    }
	}

	function fail() {
	    console.log("This method is called if something went wrong during the general communication.");
	}

	function generateRequest(req){
		console.log(req);
		var isChangeRequest = !!req.message; // true iff the object has the field

		return $("<tr data-toggle=\"collapse\" data-target=\"#accordion"+req.ID+"\" class=\"clickable\">"+
                                            "<td>"+req.requestable.name+"</td>"+
                                            "<td>"+req.timeStamp+"</td>"+
                                            "<td>"+(isChangeRequest?"Change":"Speech")+"</td>"+
                                            "<td>"+req.open+"</td>"+
                                        "</tr>"+
                                        "<tr>"+
                                            "<td colspan=\"4\">"+
                                                "<div id=\"accordion"+req.ID+"\" class=\"collapse\">"+
                                                    "<h4 style=\"color:grey;\">Attendee: "+req.requester.name+"</h4>"+
                                                    (isChangeRequest?"<h4 style=\"color:grey;\">Request Text: "+req.message+"</h4>":"")+
                                                "</div>"+
                                            "</td>"+
                                        "</tr>"

	);
	}

});
