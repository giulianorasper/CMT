import CommunicationManager from "../../communication/CommunicationManager.js";
import GetAllRequestsRequestPacket from "../../communication/packets/admin/GetAllRequestsRequestPacket.js";
import SetRequestStatusRequestPacket from "../../communication/packets/admin/SetRequestStatusRequestPacket.js";

var requestContainer = $('#requestContainer');
var requestableSelect = $('#requestableType')
var requestTypeSelect = $('#requestType')

$( document ).ready(function() {

    window.approveRequest = function(id){submitUpdate(id, true, false);}// export the function to the global scope
    window.disapproveRequest = function(id){submitUpdate(id, true, false);}// export the function to the global scope
    window.closeRequest = function(id){submitUpdate(id, true, false);}// export the function to the global scope
    window.rerenderRequests = renderRequests;

	renderRequests();

});


function renderRequests(){


	console.log("count")

	const packet = new GetAllRequestsRequestPacket();
    CommunicationManager.send(packet, success, fail);


	var options =[]


 	function success(packet) {
	    if(packet.result === "Valid") {  
	    	var selectedRequestName = requestableSelect.find('option:selected');
	    	var id = selectedRequestName.attr("data-id");
	    	

	    	requestContainer.html("");
			requestableSelect.html("<option data-type =\'\'>All requests</option>");



	    	//filter by type
			var selectedRequestType = requestTypeSelect.find('option:selected');
	    	if(selectedRequestType.attr("data-type") === "change"){
	    		packet.requests = packet.requests.filter(function(r){return !!r.message})
	    	}
	    	else if(selectedRequestType.attr("data-type") === "speech"){
	    		packet.requests = packet.requests.filter(function(r){return !r.message})
	    	}



	    	//filter by name
	    	if(id){
	    		packet.requests = packet.requests.filter(function(r){return r.requestable.name === id;})
	    	}
	    	

			//open requests firsts 
	    	packet.requests.sort(function(a,b) {if(a.open && !b.open){return -1;} else if (!a.open && b.open) {return 1;} else{return 0}})   

	        for(var req of packet.requests){
	        	 generateRequest(req).appendTo(requestContainer);
	        }

	        if(!id){
	        	return;
	        }

	        for(var i = 0; i < requestableSelect.children("option").length; i++){
	        	if($(requestableSelect.children("option")[i]).attr("data-id") === id){
	        		requestableSelect.val($(requestableSelect.children("option")[i]).text());
	        		break;
	        	}
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
 
		if(!options.includes(req.requestable.name)){
			options.push(req.requestable.name)
	 		$("<option data-id=\""+req.requestable.name+"\">" +req.requestable.name+"</option>").appendTo(requestableSelect);
	 	}
		

		var isChangeRequest = !!req.message; // true iff the object has the field

		return $("<tr data-toggle=\"collapse\" data-target=\"#accordion"+req.ID+"\" class=\"clickable\">"+
                                            "<td>"+req.requestable.name+"</td>"+
                                            "<td>"+(day + " " + month +" " + hour+":"+min+":"+sec)+"</td>"+
                                            "<td>"+(isChangeRequest?"Change":"Speech")+"</td>"+
                                            "<td>"+(isChangeRequest?(req.open?"open":(req.approved?"approved":"disapproved")):(req.open?"open":"closed"))+"</td>"+
                                            "<td>"+
                                            (req.open?(isChangeRequest?
                                                 "<span onclick = \"approveRequest(\'"+req.ID+"\')\" class=\"glyphicon glyphicon-ok \"></span>"+
                                                 "<span style= 'display:inline-block; min-width:20px'></span>"+
                                                 "<span onclick = \"disapproveRequest(\'"+req.ID+"\')\" class=\"glyphicon glyphicon glyphicon-remove \"></span>":
                                                 "<span onclick = \"closeRequest(\'"+req.ID+"\')\" class=\"glyphicon glyphicon-ok \"></span>"):"")
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
}

function submitUpdate(id, approved, open){
	

	const packet = new SetRequestStatusRequestPacket(id, approved, open);
    CommunicationManager.send(packet, success, fail);


    function success(packet) {
        console.log(packet);
        if(packet.result === "Valid") {
               renderRequests();
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }
}
