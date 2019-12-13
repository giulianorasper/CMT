import CommunicationManager from "../../communication/CommunicationManager.js";
import IsAdminRequestPacket from "../../communication/packets/IsAdminRequestPacket.js";
import LoginRequestPacket from "../../communication/packets/LoginRequestPacket.js";
import Cookies from "../../communication/utils/Cookies.js";

$( document ).ready(function() {

    const packet = new IsAdminRequestPacket();

    CommunicationManager.send(packet, success, fail);

    function success(packet) {
    	console.log(packet);
	    if(packet.result === "Valid") {
	    	 window.location = "./home.html"
	    }
	}

	function fail() {
	    console.log("This method is called if something went wrong during the general communication.");
	}


});

            $("#test-form").submit(function (e) {
                //This prevents the default redirection due to form submission
                e.preventDefault();

                var name = document.getElementById("name").value;
                var password = document.getElementById("password").value;

                function success(packet) {
                    console.log("This method is called if a response from the server is received.");
                    // Take a look at the java docs for details on the structure of the packets.
                    if(packet.result === "Valid") {
                        Cookies.setCookie("token", packet.token, packet.expiration);
                        window.location.href = "./home.html";
                    } else {
                        $("#name").focus();
                        $('#message').html('Incorrect password').css('color', 'red');
                        return false;
                    }
                }

                function fail() {
                    console.log("This method is called if something went wrong during the general communication.");
                    $('#message').html('Backend communication failed').css('color', 'red');
                }

                const login = new LoginRequestPacket(name, password);

                // Send the request to the server
                CommunicationManager.send(login, success, fail);
            });



