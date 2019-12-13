import CommunicationManager from "../../communication/CommunicationManager.js";
import IsAdminRequestPacket from "../../communication/packets/IsAdminRequestPacket.js";


$( document ).ready(function() {

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


});