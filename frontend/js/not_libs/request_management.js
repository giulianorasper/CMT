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
		var dateObj = new Date(req.timeStamp * 1000); 
		var split = ("" + dateObj).split(" ");
		var day = split[2];
		var month = split[1];
		var hour = split[4].split(":")[0];
		var min = split[4].split(":")[1];
		var sec = split[4].split(":")[2];
 
		

		console.log(dateObj)
		var isChangeRequest = !!req.message; // true iff the object has the field

		return $("<tr data-toggle=\"collapse\" data-target=\"#accordion"+req.ID+"\" class=\"clickable\">"+
                                            "<td>"+req.requestable.name+"</td>"+
                                            "<td>"+(day + " " + month +" " + hour+":"+min+":"+sec)+"</td>"+
                                            "<td>"+(isChangeRequest?"Change":"Speech")+"</td>"+
                                            "<td>"+req.open+"</td>"+
                                            "<td>"+
                                            (isChangeRequest?
                                                 "<span onclick = \"downloadDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-ok \"></span>"+
                                                 "<span style= 'display:inline-block; min-width:20px'></span>"+
                                                 "<span onclick = \"downloadDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon glyphicon-remove \"></span>":
                                                 "<span onclick = \"downloadDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-ok \"></span>")
                                                 +
                                            "</td>"+
                                        "</tr>"+
                                        "<tr>"+
                                            "<td colspan=\"5\">"+
                                                "<div id=\"accordion"+req.ID+"\" class=\"collapse\">"+
                                                    "<h4 style=\"color:grey;\">Attendee: "+req.requester.name+"</h4>"+
                                                    (isChangeRequest?"<h4 style=\"color:grey;\">Request Text: "+req.message+"</h4>":"")+
                                                "</div>"+
                                            "</td>"+
                                        "</tr>"

	);
	}

});
