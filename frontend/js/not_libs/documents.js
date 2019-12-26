import CommunicationManager from "../../communication/CommunicationManager.js";
import GetDocumentListRequestPacket from "../../communication/packets/GetDocumentListRequestPacket.js";
import UploadFileRequestPacket from "../../communication/packets/admin/UploadFileRequestPacket.js";
import GetFileRequestPacket from "../../communication/packets/DownloadFileRequestPacket.js";
import DeleteFileRequestPacket from "../../communication/packets/admin/DeleteFileRequestPacket.js";
import IsAdminRequestPacket from "../../communication/packets/IsAdminRequestPacket.js";



var documentContainer = $('#documentsContainer');


$( document ).ready(function() {

    window.downloadDocument = download// export the function to the global scope
    window.editDocument = edit// export the function to the global scope
    window.removeDocument = remove// export the function to the global scope


    checkAdminStatus();

    const packet = new GetDocumentListRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet) {
	    if(packet.result === "Valid") {
            console.log(packet.documents.length);
        if(packet.documents.length === 0){
            $("<div class=\"col-lg-9\">"+"Currently no document is available"+"</div>").appendTo(documentContainer);
            return;
        }          
	        for(var doc of packet.documents){
	        	console.log(generateDocument(doc));
	        	generateDocument(doc).appendTo(documentContainer);
	        }
	    }
	}

	function fail() {
	    console.log("This method is called if something went wrong during the general communication.");
	}

	let files = null;
	document.getElementById('uploadFile').addEventListener('change', function (event) {
	    files = event.target.files;
	    console.log(files);
    }, false);

    document.getElementById('submitUpload').onclick = () => {
        if(files == null) {
            console.log("implement feedback");
            //TODO feedback
            return;
        }
        console.log(files);

        // Initialize an instance of the `FileReader`
        const reader = new FileReader();

        // Specify the handler for the `load` event
        reader.onload = function (e) {

            function success(packet) {
                location.reload();
                console.log("This method is called if a response from the server is received.");
                //TODO implement feedback
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
    }

});

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
                                            "</div>"+(window.isAdmin?
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span onclick = \"editDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-edit\"></span>"+
                                                    "</a>"+
                                            "</div>"+
                                            "<div class=\"col-lg-1\">"+
                                                       "<a href=\"#\" style=\"color: #00D363; font-size: 25px;\">"+
                                                      "<span onclick = \"removeDocument(\'"+document.name+"\')\" class=\"glyphicon glyphicon-trash \"></span>"+
                                                    "</a>":"")+
                                            "</div>"+
                                        "</div>");
}
