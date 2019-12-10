import CommunicationManager from "../../communication/CommunicationManager.js";
import GetDocumentListRequestPacket from "../../communication/packets/GetDocumentListRequestPacket.js";
import UploadFileRequestPacket from "../../communication/packets/admin/UploadFileRequestPacket.js";

var documentContainer = $('#documentsContainer');

$( document ).ready(function() {

    const packet = new GetDocumentListRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet) {
	    if(packet.result === "Valid") {          
	        for(var doc of packet.documents){
	        	console.log(generateDocument(doc));
	        	generateDocument(doc).appendTo(documentContainer);
	        }
	    }
	}

	function fail() {
	    console.log("This method is called if something went wrong during the general communication.");
	}

	document.getElementById('uploadFile').addEventListener('change', function (event) {

        const files = event.target.files;

        // Initialize an instance of the `FileReader`
        const reader = new FileReader();

        // Specify the handler for the `load` event
        reader.onload = function (e) {

            function success(packet) {
                console.log("This method is called if a response from the server is received.");
            }

            function fail() {
                console.log("This method is called if something went wrong during the general communication.");
            }
            console.log(e.target)
            const packet = new UploadFileRequestPacket(files[0].name, files[0].name , e.target.result, true);
            console.log(files[0].name)

            // Send the request to the server
            CommunicationManager.send(packet, success, fail);


        }

        // Read the file
        reader.readAsArrayBuffer(files[0]);
    }, false);

});




function generateDocument(document){
	return $("<div class=\"row\">"+
                                            "<div class=\"col-lg-9\">"+
                                                "<li>"+document.name+"</li>"+
                                            "</div>"+

                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span class=\"glyphicon glyphicon-download-alt \"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span class=\"glyphicon glyphicon-edit\"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span class=\"glyphicon glyphicon-trash \"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                        "</div>");
}