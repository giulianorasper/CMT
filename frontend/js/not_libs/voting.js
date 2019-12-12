import CommunicationManager from "../../communication/CommunicationManager.js";
import GetActiveVotingRequestPacket from "../../communication/packets/GetActiveVotingRequestPacket.js";
import AddVoteRequestPacket from "../../communication/packets/AddVoteRequestPacket.js";

var optionList;
var voteID;
var dateObject;

$(document).ready( function() {

    function success(packet){
        console.log(packet)
        if(packet.result === "Valid"){
            displayActiveVote(packet);

        }
    }

    function fail() {
        console.log("Something went wrong during, get active vote question & options.");
    }

    const getActiveVote = new GetActiveVotingRequestPacket();

    CommunicationManager.send(getActiveVote, success, fail);

});


function displayActiveVote(packet){
	// packet.exists,
	// packet.voting.id
	dateObject = packet.voting.openUntil;
	
	if(packet.exists){
		
		//var today = new Date(packet.voting.openUntil * 1000);
		
		
		//var voteDateOnly = voteDate.toUTCString();
		
		//console.log(voteDate.toUTCString());

		//console.log(packet.voting.id);

		optionList = packet.voting.options;
		console.log(optionList[0]);
		
		voteID = packet.voting.ID;
		
		//$("#voteQuestion").html('<h2 class="contact-title pull-left">'+ packet.voting.question + '</h2>')
		$("#voteQuestion").html('<div class="row"><div class="col-lg-2" style="float:left;"></div><div class="col-lg-10" style="float:left; padding-top: 50px;" id="'+packet.voting.ID+'s"><h2 class="contact-title pull-left">'+packet.voting.question+'</h2></div></div>');
		 
		for(var i in packet.voting.options){
		
		var questionOptions = '<div class="row"><div class="col-lg-2"></div><div class="custom-control custom-radio col-lg-10"><div class="form-group"><input type="radio" class="custom-control-input" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;"><label class="custom-control-label" for="'+packet.voting.options[i].optionID+'">'+packet.voting.options[i].name+'</label></div></div></div>';
		$('#options').append(questionOptions);
		
		}	 
               		
	}
	else {
		
		$("#submit-message").empty();
		$("#submit-message").addClass("row").addClass("contact-title");
		$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Currently no active vote!</h2>");
	}
	
}


        //$("#form-submit").submit(function (e) {
        $("#submitButton").on("click", function () {
			
                //e.preventDefault();
				
				
				const selectedOptionId = $('input[name="radio"]:checked').attr('id');
				
				//const voteId = $('#voteQuestion').attr('id');
				//const voteId = $('#voteQuestion').find(":nth-child(2)"); 
				
				//console.log(selectedOptionId);
				//console.log(questionID);
				//console.log(voteId);
				//console.log(optionList[selectedOptionId].optionID);
				
				var voteDate = new Date(dateObject);
				var currentDateOnly = new Date();
				
				console.log(voteDate.toUTCString());
				console.log(currentDateOnly.toUTCString());
				
							//	if(voteDate.toUTCString() <= currentDateOnly.toUTCString())
								
				console.log(voteDate.toUTCString() <= currentDateOnly.toUTCString());

				    function success(packet){
						if(packet.result === "Valid" && voteDate.toUTCString() < currentDateOnly.toUTCString()){
							
							$("#submit-message").empty();
							$("#submit-message").addClass("row").addClass("contact-title");
							$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Vote Submitted!</h2>");
						}
						else if(packet.result === "Valid" && voteDate.toUTCString() >= currentDateOnly.toUTCString()) {
							$("#failure").html("<h4 style='float: right; margin-top:30px;'>Vote has been expired!</h4>");
							
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
		