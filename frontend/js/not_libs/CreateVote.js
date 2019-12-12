import CommunicationManager from "../../communication/CommunicationManager.js";
import AddVotingRequestPacket from "../../communication/packets/admin/AddVotingRequestPacket.js";


$(document).ready(function() {

		
		$(function() {
			$( "#my_date_picker" ).datepicker();
	//		$( "#my_date_picker" ).datetimepicker();
		});



        $("#createVote").on("click", function () {
			
				var optionsList = [];
				var voteType;
				var voteQuestion = $('#voteQuestion').val();
				var expirtyDate = $('#my_date_picker').val();

				
				if($('input[name="radio"]:checked').attr('id') == 1){
					var voteType = true;
				} else {
					var voteType = false;
				}
					
				
				$('#options option').each(function() { 
					optionsList.push( $(this).attr('value') );
				});
				
				console.log(voteQuestion);
				console.log(expirtyDate);
				console.log(optionsList);
				console.log(voteType);
			
				function success(packet){
						console.log(packet);

						if(packet.result === "Valid"){

							$("#successful").html("<h4 style='float: right; margin-top:30px;'>New vote has been created successfully!</h4>");
							
						}

					}

					function fail() {
						console.log("Try again! vote is not created");
					}
				
				
				const CreateVote = new AddVotingRequestPacket(voteQuestion, optionsList, voteType, expirtyDate);

				CommunicationManager.send(CreateVote, success, fail); 
				
        });
		
});