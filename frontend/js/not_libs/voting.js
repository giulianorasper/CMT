import CommunicationManager from "../../communication/CommunicationManager.js";
import GetActiveVotingRequestPacket from "../../communication/packets/GetActiveVotingRequestPacket.js";
import AddVoteRequestPacket from "../../communication/packets/AddVoteRequestPacket.js";

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
	console.log(voting.id);
	
	if(packet.exists){
		
		//$("#voteQuestion").html('<h2 class="contact-title pull-left">'+ packet.voting.question + '</h2>')
		$("#voteQuestion").html('<div class="row"><div class="col-lg-2" style="float:left;"></div><div class="col-lg-10" style="float:left; padding-top: 50px;" id="'+packet.voting.id+'"><h2 class="contact-title pull-left">'+packet.voting.question+'</h2></div></div>');
		 
		for(var i in packet.voting.options){
		
		var questionOptions = '<div class="row"><div class="col-lg-2"></div><div class="custom-control custom-radio col-lg-10"><div class="form-group"><input type="radio" class="custom-control-input" id="'+packet.voting.options[i].optionID+'" checked name="radio" style="background:#2E004B;"><label class="custom-control-label" for="1">'+packet.voting.options[i].name+'</label></div></div></div>';
		$('#options > .col-lg-9').html(document.write(questionOptions));
		
		}	 
               		
	}
	else {
		
		$("#submit-message").empty();
		$("#submit-message").addClass("row").addClass("contact-title");
		$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Currently no active vote!</h2>");
	}
	
}


        $("#form-submit").submit(function (e) {
			
                e.preventDefault();
				
				selectedOptionId = $('input[name="radio"]:checked').val();
				voteId = $('#voteQuestion').attr('id');
				
				    function success(packet){
						console.log(packet)
						if(packet.result === "Valid"){
							$("#submit-message").empty();
							$("#submit-message").addClass("row").addClass("contact-title");
							$("#submit-message").append("<h2 class='contact-title' style='margin-left: 40px;'>Vote Submitted!</h2>");
						}
					}

					function fail() {
						console.log("sorry! your vote are not sumbiited");
					}
				
				const sendVote = new AddVoteRequestPacket(voteId, selectedOptionId);

				CommunicationManager.send(sendVote, success, fail);
				
				//$( "input[type='radio']" ).on( "click", function(){
					
					//selectedOption = $(this).val();
					
				//});
                    return false;


        });