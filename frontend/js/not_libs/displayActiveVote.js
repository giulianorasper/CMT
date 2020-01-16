import CommunicationManager from "../../communication/CommunicationManager.js";
import GetActiveVotingRequestPacket from "../../communication/packets/GetActiveVotingRequestPacket.js";
import AddVoteRequestPacket from "../../communication/packets/AddVoteRequestPacket.js";
import { getPreviousVote } from "./displayPreviousVote.js";

var optionList;
var voteID;
// var dateObject;
var timeOut = false;

var packetAssign;
var currentDifference;


// var testingVar;

 
/* export const values = "testing";
	
export function ActiveVotePacketCall(){
	
	console.log("working");
} */

/**
 * A request will be send to server for Active Vote.
 * If the packet result is valid in response then it will be passed as a parameter to displayActiveVote function in order to display a vote. 
 */
	
$(document).ready( function() {
	 
	
	function success(packet){

		if(packet.result === "Valid"){
			
			// export testingVar = true;
			
			// window.location.href = './vote.html';
			
			// var voteExpiryDate = new Date(packet.voting.openUntil);
			
			// console.log(voteExpiryDate.toLocaleTimeString());
			
			// packet is exposed to global scope  later use.
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

/**
 * Purpose of this function is to countdown a vote duration and to display it.
 * sessionstorage is used to store and retreive seconds for timer.
 * This function will take only one paramter i.e.
 * @param seconds
 * Once the timer ends it will call displayActiveVote function again to display vote expiry message.
 * This will accept only one argument i.e.
 * @param packetAssign
 * packetAssign is already declared in a global scope.
 */

function countdown(seconds) {
  seconds = parseInt(sessionStorage.getItem("seconds"))||seconds;

  function tick() {
    seconds--; 
    sessionStorage.setItem("seconds", seconds)
    var counter = document.getElementById("timer");
	
	// In case hour is required.
	// var hours = Math.floor(seconds / (60 * 60));
	
    var current_minutes = parseInt(seconds/60);
    var current_seconds = seconds % 60;
    counter.innerHTML = "Time Left: " + "00hr: " + current_minutes + "min: " + (current_seconds < 10 ? "0" : "") + current_seconds + "sec";
	// $('#voteQuestion').html('<div class="col-lg-12 " style="padding-top:50px; float:right; font-size: 25px; margin-right:30px; text-align: right;"></div>'+ 
								// current_minutes + ":" + (current_seconds < 10 ? "0" : "") + current_seconds);
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

/**
 * this function will handle cookies for vote expiry and session/localStorage for storing a value for timer that will be used as a flag to redirect to vote page once it starts.
 * This will accept only one argument i.e.
 * @param packet
 */
function cookiesSessionHandling(packet){
	
		sessionStorage.setItem("redirect", true)
		// var voteExpiryDate = new Date(packet.voting.openUntil);
		var voteExpiryDate = new Date(packet.voting.openUntil);
		
		console.log(voteExpiryDate.toGMTString());
		console.log(voteExpiryDate);
		var currentDateOnly = new Date();
		console.log(packet.voting.openUntil);
		console.log(currentDateOnly);
		// console.log(currentDateOnly.getTime());
		var diff = Math.abs(voteExpiryDate.getTime() - currentDateOnly.getTime());
		console.log(diff);
		currentDifference = Math.round(diff/1000);
		console.log(currentDifference);

		document.cookie = "voteID=" + packet.voting.ID + ";path=./vote.html;expires=" + voteExpiryDate.toGMTString();
	
	
	
}

/**
 * displayActiveVote function will display vote with options and also countdown. In case vote is not expired otherwise will display only expire message.
 * This will accept only one argument i.e.
 * @param packet
 * It will call cookiesSessionHandling function to check that vote is expired or not. In case vote is not expired will display vote with available options and also countdown otherwise will display only expire message.
 * This will accept only one argument i.e.
 * @param packet 
 */
 
function displayActiveVote(packet){

	if(packet.exists){
		console.log(packet);
		

		cookiesSessionHandling(packet);
		
		if( document.cookie && document.cookie.indexOf('voteID='+packet.voting.ID+'') != -1 ) {
			
				// if cookie is not expired
			sessionStorage.removeItem('seconds');

			countdown(currentDifference)
			
			optionList = packet.voting.options;
		
			voteID = packet.voting.ID
			
			$('#options').empty();
			$("#voteQuestion").html('<div class="col-lg-12" style="margin-left:30px; font-size:25px; padding-bottom:10px; word-break:break-word; margin-right:30px; margin-top: 10px;" id="'+packet.voting.ID+'">'+
									'<span style="word-break:break-word; margin-right:30px;" class="d-flex p-2">'+packet.voting.question+
									'</span>'+
									'</div>');
			
			// $("#voteQuestion").html('<div class="row"><div class="col-lg-2" style="float:left;"></div><div class="col-lg-10" style="float:left; padding-top: 50px;" id="'+packet.voting.ID+'"><h2 class="contact-title pull-left">'+packet.voting.question+'</h2></div></div>');
			 
			for(var i in packet.voting.options){
			
				var questionOptions = '<div class="row">'+
										'<div class="col-lg-12">'+
										'<div class="custom-control custom-radio" style="margin-left:50px;">'+
										'<input type="radio" class="custom-control-input d-flex p-2" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;">'+
										'<label style="word-break: break-word; margin-right: 30px;" class="custom-control-label" for="'+packet.voting.options[i].optionID+'">'+packet.voting.options[i].name+'</label>'+
										'</div>'+
										'</div>'+
										'</div>';
				
				// var questionOptions = '<div class="row"><div class="col-lg-2"></div><div class="custom-control custom-radio col-lg-10"><div class="form-group"><input type="radio" class="custom-control-input" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;"><label class="custom-control-label" for="'+packet.voting.options[i].optionID+'">'+packet.voting.options[i].name+'</label></div></div></div>';
				$('#voteQuestion').append(questionOptions);
			
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


/**
 * this Anonymous function will be called when click on submit vote button. Purpose of this function is to send attendee selected data to server.
 * The following paramters should be included in the request.
 * @param voteID
 * @param selectedOptionId 
 */

        //$("#form-submit").submit(function (e) {
        $("#submitButton").on("click", function () {
			
                //e.preventDefault();
				
				
				const selectedOptionId = $('input[name="radio"]:checked').attr('id');
				

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
	
        });
		
