import CommunicationManager from "../../communication/CommunicationManager.js";
import GetActiveVotingRequestPacket from "../../communication/packets/GetActiveVotingRequestPacket.js";
import AddVoteRequestPacket from "../../communication/packets/AddVoteRequestPacket.js";

var optionList;
var voteID;
var dateObject;
var timeOut = false;

var packetAssign;
var currentDifference;


// var testingVar;

 
/* export const values = "testing";
	
export function ActiveVotePacketCall(){
	
	console.log("working");
} */
	
$(document).ready( function() {
	 
	
	function success(packet){

		if(packet.result === "Valid"){
			
			// export testingVar = true;
			
			// window.location.href = './vote.html';
			
			// var voteExpiryDate = new Date(packet.voting.openUntil);
			
			// console.log(voteExpiryDate.toLocaleTimeString());
			packetAssign = packet;
			
			displayActiveVote(packet);


		}
    }

    function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

    const getActiveVote = new GetActiveVotingRequestPacket();

    CommunicationManager.send(getActiveVote, success, fail); 
		
		
	});


 // Countdown function is used to countdown i.e. that '---' time is remaining in vote expiration.


function countdown(seconds) {
  seconds = parseInt(sessionStorage.getItem("seconds"))||seconds;

  function tick() {
    seconds--; 
    sessionStorage.setItem("seconds", seconds)
    var counter = document.getElementById("timer");
    var current_minutes = parseInt(seconds/60);
    var current_seconds = seconds % 60;
    counter.innerHTML = current_minutes + ":" + (current_seconds < 10 ? "0" : "") + current_seconds;
    if( seconds > 0 ) {
      setTimeout(tick, 1000);
    }
	else {
		
		timeOut = true;

		displayActiveVote(packetAssign);
		// sessionStorage.setItem("timeOut", timeOut)
	}
	
	
  }
  tick();
}
// this function will handle cookies for vote expiry and session/localStorage save a value that is used as flag to redirect to vote page once if it starts.
function cookiesSessionHandling(packet){
	
		localStorage.setItem("redirect", true)
		var voteExpiryDate = new Date(packet.voting.openUntil);
		
		var currentDateOnly = new Date();
		
		// console.log(currentDateOnly.getTime());
		var diff = Math.abs(voteExpiryDate.getTime() - currentDateOnly.getTime());
		currentDifference = Math.round(diff/1000);

		document.cookie = "voteID=" + packet.voting.ID + ";path=./vote.html;expires=" + voteExpiryDate.toGMTString();
	
	
	
}

// displayActiveVote will dispplay vote with options and also countdown timer. 
// In addition, will also store cookies for the vote i.e. contains voteID, path and vote expiration time.

function displayActiveVote(packet){

	if(packet.exists){
		

		cookiesSessionHandling(packet);
		
		if( document.cookie && document.cookie.indexOf('voteID='+packet.voting.ID+'') != -1 ) {
			
				// if cookie is not expired
			sessionStorage.removeItem('seconds');

			countdown(currentDifference)
			
			optionList = packet.voting.options;
		
			voteID = packet.voting.ID
			
			$('#options').empty();
			
			$("#voteQuestion").html('<div class="row"><div class="col-lg-2" style="float:left;"></div><div class="col-lg-10" style="float:left; padding-top: 50px;" id="'+packet.voting.ID+'s"><h2 class="contact-title pull-left">'+packet.voting.question+'</h2></div></div>');
			 
			for(var i in packet.voting.options){
			
				var questionOptions = '<div class="row"><div class="col-lg-2"></div><div class="custom-control custom-radio col-lg-10"><div class="form-group"><input type="radio" class="custom-control-input" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;"><label class="custom-control-label" for="'+packet.voting.options[i].optionID+'">'+packet.voting.options[i].name+'</label></div></div></div>';
				$('#options').append(questionOptions);
			
			}

		} else {
		// DO SOMETHING ELSE

			$("#submit-message").empty();
			$("#submit-message").addClass("row").addClass("contact-title");
			$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Vote has been expired!</h2>");

		}
		

               		
	}
	else {
		
		$("#submit-message").empty();
		$("#submit-message").addClass("row").addClass("contact-title");
		$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Currently no active vote!</h2>");
	}
	
}

// this Anonymous function will be called when click on submit vote button.
// Further, data will be to the backend and will display the response successfull message.

        //$("#form-submit").submit(function (e) {
        $("#submitButton").on("click", function () {
			
                //e.preventDefault();
				
				
				const selectedOptionId = $('input[name="radio"]:checked').attr('id');
				

				if(timeOut){
					$("#failure").html("<h4 style='float: right; margin-top:30px;'>Vote has been expired!</h4>");		
				}
				
				else {
					
					function success(packet){
						if(packet.result === "Valid"){
							
							$("#submit-message").empty();
							$("#submit-message").addClass("row").addClass("contact-title");
							$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Vote Submitted!</h2>");
						}

						else{
							$("#failure").html("<h4 style='float: right; margin-top:30px;'>You have already submitted vote!</h4>");	
						}
					}

					function fail() {
						console.log("sorry! your vote is not sumbiited");
					}
					
					const sendVote = new AddVoteRequestPacket(voteID, selectedOptionId);

					CommunicationManager.send(sendVote, success, fail);	
					
				}
				    
				
				

        });
		
