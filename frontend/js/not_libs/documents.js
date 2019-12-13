import CommunicationManager from "../../communication/CommunicationManager.js";
import GetDocumentListRequestPacket from "../../communication/packets/GetDocumentListRequestPacket.js";
import UploadFileRequestPacket from "../../communication/packets/admin/UploadFileRequestPacket.js";
import GetFileRequestPacket from "../../communication/packets/DownloadFileRequestPacket.js";
import DeleteFileRequestPacket from "../../communication/packets/admin/DeleteFileRequestPacket.js";

var documentContainer = $('#documentsContainer');

$( document ).ready(function() {

    window.downloadDocument = download// export the function to the global scope
    window.editDocument = edit// export the function to the global scope
    window.removeDocument = remove// export the function to the global scope

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
        console.log(files);

        // Initialize an instance of the `FileReader`
        const reader = new FileReader();

        // Specify the handler for the `load` event
        reader.onload = function (e) {

            if (confirm('Are you sure you wish to upload \"'+files[0].name+'\"?')) {
                
            } else {
                $(document.getElementById('uploadFile')).val("")
                return;
            }

            function success(packet) {
                location.reload();
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

function edit(name){

}

function remove(name){
       
    const packet = new DeleteFileRequestPacket(name);
    CommunicationManager.send(packet, success, fail);
    

    function success(packet) {
        console.log(packet);
        if(packet.result === "Valid") {
                location.reload();
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }
}


function download(name){
    function success(packet) {
        console.log("This method is called if a response from the server is received.");
        if(packet.result === "Valid") {
            var bytes = new Uint8Array(packet.fileBytes);

            //var blob=new Blob([bytes], {type: "application/pdf"});
            var blob=new Blob([bytes]);

            var link=document.createElement('a');
            link.href=window.URL.createObjectURL(blob);
            link.download=packet.fileName;
            link.click();
        }
    }

    function fail() {
        console.log("This method is called if something went wrong during the general communication.");
    }

    const packet = new GetFileRequestPacket(name);

    // Send the request to the server
    CommunicationManager.send(packet, success, fail);
}



function generateDocument(document){
	return $("<div class=\"row\">"+
                                            "<div class=\"col-lg-9\">"+
                                                "<li>"+document.name+"</li>"+
                                            "</div>"+

                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span onclick = \"downloadDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-download-alt \"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span onclick = \"editDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-edit\"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span onclick = \"removeDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-trash \"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                        "</div>");
}